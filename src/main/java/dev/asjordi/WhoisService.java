package dev.asjordi;

import org.apache.commons.net.whois.WhoisClient;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Optional;

/**
 * Provides a service for performing WHOIS queries.
 * <p>
 * This class utilizes a {@link WhoisCache} to retrieve WHOIS server information
 * based on domain extensions. It performs WHOIS queries using the Apache Commons
 * Net library and returns the raw WHOIS response.
 * </p>
 * <p>
 * The service handles domain validation, sanitization, and fallback mechanisms
 * for WHOIS servers. If no specific WHOIS server is found for a domain extension,
 * it defaults to querying the IANA WHOIS server.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 *     WhoisService whoisService = new WhoisService();
 *     Optional&lt;String&gt; response = whoisService.performWhoisQuery("example.com");
 *     response.ifPresent(System.out::println);
 * </pre>
 * </p>
 */
public class WhoisService {

    private final WhoisCache whoisCache;

    public WhoisService() {
        this.whoisCache = new WhoisCache();
    }

    /**
     * Performs a WHOIS query for the given domain and returns the raw response.
     *
     * @param domain The domain to query.
     * @return An Optional containing the raw WHOIS response, or empty if the domain is invalid or the query fails.
     * @throws IOException If an error occurs during the query.
     */
    public Optional<String> performWhoisQuery(String domain) throws IOException {
        if (domain == null || domain.isBlank() || !DomainValidatorUtil.isValidDomain(domain)) return Optional.empty();

        domain = DomainSanitizer.sanitize(domain);
        String domainExtension = domain.substring(domain.lastIndexOf('.'));

        String whoisServer = whoisCache
                .getWhoisServer(domainExtension)
                .orElse("whois.iana.org");

        WhoisClient whoisClient = new WhoisClient();

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
