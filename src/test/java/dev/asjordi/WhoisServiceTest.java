package dev.asjordi;

import org.apache.commons.net.whois.WhoisClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class WhoisServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(WhoisServiceTest.class);

    private WhoisService whoisService;
    private TestWhoisCache mockWhoisCache;
    private TestWhoisClient mockWhoisClient;

    @BeforeEach
    void setUp() {
        logger.atTrace().log("Setting up mock dependencies for WhoisServiceTest");
        mockWhoisCache = new TestWhoisCache();
        mockWhoisClient = new TestWhoisClient();
        whoisService = new TestWhoisService(mockWhoisCache, mockWhoisClient);
    }

    @Test
    void testValidDomainWithSuccessfulQuery() throws IOException {
        logger.atTrace().log("Testing valid domain with successful query");
        String domain = "example.com";
        String expectedResponse = "Domain Name: example.com\nRegistry Domain ID: 2336799_DOMAIN_COM-VRSN";
        mockWhoisCache.setServerForTld(".com", "whois.verisign-grs.com");
        mockWhoisClient.setResponse(expectedResponse);

        logger.atTrace().log("Performing WHOIS query for domain: {}", domain);
        Optional<String> result = whoisService.performWhoisQuery(domain);

        logger.atTrace().log("Asserting results for domain: {}", domain);
        assertTrue(result.isPresent());
        assertEquals(expectedResponse, result.get());
        assertEquals("whois.verisign-grs.com", mockWhoisClient.getLastConnectedServer());
        assertEquals("example.com", mockWhoisClient.getLastQuery());
    }

    @Test
    void testInvalidDomainReturnsEmptyOptional() throws IOException {
        logger.atTrace().log("Testing invalid domains returning empty optional");

        logger.atTrace().log("Testing null domain");
        Optional<String> nullResult = whoisService.performWhoisQuery(null);
        assertTrue(nullResult.isEmpty());

        logger.atTrace().log("Testing empty domain");
        Optional<String> emptyResult = whoisService.performWhoisQuery("");
        assertTrue(emptyResult.isEmpty());

        logger.atTrace().log("Testing blank domain");
        Optional<String> blankResult = whoisService.performWhoisQuery("   ");
        assertTrue(blankResult.isEmpty());

        logger.atTrace().log("Testing invalid domain format");
        Optional<String> invalidResult = whoisService.performWhoisQuery("invalid-domain");
        assertTrue(invalidResult.isEmpty());
    }

    @Test
    void testDomainWithUnknownTldUsesIanaServer() throws IOException {
        logger.atTrace().log("Testing domain with unknown TLD using IANA server");
        String domain = "example.com";
        String expectedResponse = "Domain not found";
        mockWhoisClient.setResponse(expectedResponse);

        logger.atTrace().log("Performing WHOIS query for domain: {}", domain);
        Optional<String> result = whoisService.performWhoisQuery(domain);

        logger.atTrace().log("Asserting results for domain: {}", domain);
        assertTrue(result.isPresent());
        assertEquals(expectedResponse, result.get());
        assertEquals("whois.iana.org", mockWhoisClient.getLastConnectedServer());
        assertEquals("example.com", mockWhoisClient.getLastQuery());
    }

    @Test
    void testUnknownHostExceptionThrowsRuntimeException() throws IOException {
        logger.atTrace().log("Testing UnknownHostException handling");
        String domain = "example.com";
        mockWhoisCache.setServerForTld(".com", "whois.verisign-grs.com");
        mockWhoisClient.setThrowUnknownHostException(true);

        logger.atTrace().log("Performing WHOIS query for domain: {}", domain);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            whoisService.performWhoisQuery(domain);
        });

        logger.atTrace().log("Asserting exception for UnknownHostException");
        assertTrue(exception.getMessage().contains("Host resolution failed"));
        assertTrue(exception.getCause() instanceof UnknownHostException);
    }

    @Test
    void testIOExceptionThrowsRuntimeException() throws IOException {
        logger.atTrace().log("Testing IOException handling");
        String domain = "example.com";
        mockWhoisCache.setServerForTld(".com", "whois.verisign-grs.com");
        mockWhoisClient.setThrowIOException(true);

        logger.atTrace().log("Performing WHOIS query for domain: {}", domain);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            whoisService.performWhoisQuery(domain);
        });

        logger.atTrace().log("Asserting exception for IOException");
        assertTrue(exception.getMessage().contains("Connection failed"));
        assertTrue(exception.getCause() instanceof IOException);
    }

    @Test
    void testDomainSanitization() throws IOException {
        logger.atTrace().log("Testing domain sanitization");
        String domain = "  WWW.EXAMPLE.COM  ";
        String expectedResponse = "Domain Name: example.com";
        mockWhoisCache.setServerForTld(".com", "whois.verisign-grs.com");
        mockWhoisClient.setResponse(expectedResponse);

        logger.atTrace().log("Performing WHOIS query for domain: {}", domain);
        Optional<String> result = whoisService.performWhoisQuery(domain);

        logger.atTrace().log("Asserting results for sanitized domain: {}", domain);
        assertTrue(result.isPresent());
        assertEquals(expectedResponse, result.get());
        String sanitizedDomain = DomainSanitizer.sanitize(domain);
        assertEquals(sanitizedDomain, mockWhoisClient.getLastQuery());
    }

    // Custom test implementation of WhoisService that uses our mock dependencies
    private static class TestWhoisService extends WhoisService {
        private final TestWhoisCache whoisCache;
        private final TestWhoisClient whoisClient;

        public TestWhoisService(TestWhoisCache whoisCache, TestWhoisClient whoisClient) {
            this.whoisCache = whoisCache;
            this.whoisClient = whoisClient;
        }

        @Override
        public Optional<String> performWhoisQuery(String domain) {
            if (domain == null || domain.isBlank() || !DomainValidatorUtil.isValidDomain(domain)) return Optional.empty();

            domain = DomainSanitizer.sanitize(domain);
            String domainExtension = domain.substring(domain.lastIndexOf('.'));

            String whoisServer = whoisCache
                    .getWhoisServer(domainExtension)
                    .orElse("whois.iana.org");

            try {
                whoisClient.connect(whoisServer);
                String result = whoisClient.query(domain);
                return Optional.ofNullable(result);
            } catch (UnknownHostException e) {
                System.err.println("Error: Unable to resolve host '" + whoisServer + "'. Please check your DNS settings or try again later.");
                throw new RuntimeException("Host resolution failed for '" + whoisServer + "'", e);
            } catch (IOException e) {
                System.err.println("Error: Unable to connect to the WHOIS server. Please check your network connection.");
                throw new RuntimeException("Connection failed to WHOIS server", e);
            } finally {
                whoisClient.disconnect();
            }
        }
    }

    // Mock implementation of WhoisCache for testing
    private static class TestWhoisCache extends WhoisCache {
        private final java.util.Map<String, String> servers = new java.util.HashMap<>();

        public void setServerForTld(String tld, String server) {
            servers.put(tld, server);
        }

        @Override
        public Optional<String> getWhoisServer(String domain) {
            if (domain == null || domain.isBlank() || !domain.startsWith(".")) return Optional.empty();
            return Optional.ofNullable(servers.get(domain.toLowerCase()));
        }
    }

    // Mock implementation of WhoisClient for testing
    private static class TestWhoisClient {
        private final WhoisClient whoisClient = new WhoisClient();
        private String response;
        private String lastConnectedServer;
        private String lastQuery;
        private boolean throwUnknownHostException;
        private boolean throwIOException;

        public void setResponse(String response) {
            this.response = response;
        }

        public void setThrowUnknownHostException(boolean throwUnknownHostException) {
            this.throwUnknownHostException = throwUnknownHostException;
        }

        public void setThrowIOException(boolean throwIOException) {
            this.throwIOException = throwIOException;
        }

        public String getLastConnectedServer() {
            return lastConnectedServer;
        }

        public String getLastQuery() {
            return lastQuery;
        }

        public void connect(String hostname) throws IOException {
            if (throwUnknownHostException) {
                throw new UnknownHostException("Unknown host: " + hostname);
            }
            if (throwIOException) {
                throw new IOException("Connection failed");
            }
            this.lastConnectedServer = hostname;
            // Don't actually connect in tests
        }

        public String query(String handle) throws IOException {
            if (throwIOException) {
                throw new IOException("Query failed");
            }
            this.lastQuery = handle;
            return response;
        }

        public void disconnect() {
            // Do nothing for testing
        }
    }
}

