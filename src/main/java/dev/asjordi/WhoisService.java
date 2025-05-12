package dev.asjordi;

import org.apache.commons.net.whois.WhoisClient;
import dev.asjordi.exceptions.WhoisQueryException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(WhoisService.class);
    private final WhoisCache whoisCache;

    public WhoisService() {
        this.whoisCache = new WhoisCache();
    }

    /**
     * Performs a WHOIS query for the given domain and returns the raw response.
     *
     * @param domain The domain to query.
     * @return An Optional containing the raw WHOIS response, or empty if the domain is invalid or the query fails.
     * @throws WhoisQueryException If an error occurs during the query.
     */
    public Optional<String> performWhoisQuery(String domain) throws WhoisQueryException {
        logger.atDebug().log("Performing WHOIS query for domain: {}", domain);

        if (domain == null || domain.isBlank() || !DomainValidatorUtil.isValidDomain(domain)) {
            logger.atWarn().log("Invalid or blank domain: {}", domain);
            return Optional.empty();
        }

        domain = DomainSanitizer.sanitize(domain);
        logger.atDebug().log("Sanitized domain: {}", domain);

        String domainExtension = domain.substring(domain.lastIndexOf('.'));
        logger.atDebug().log("Extracted domain extension: {}", domainExtension);

        String whoisServer = whoisCache
                .getWhoisServer(domainExtension)
                .orElse("whois.iana.org");
        logger.atInfo().log("Using WHOIS server: {}", whoisServer);

        WhoisClient whoisClient = new WhoisClient();

        try {
            logger.atDebug().log("Connecting to WHOIS server: {}", whoisServer);
            whoisClient.connect(whoisServer);

            String result = whoisClient.query(domain);
            logger.atInfo().log("WHOIS query successful for domain: {}", domain);
            return Optional.ofNullable(result);
        } catch (UnknownHostException e) {
            logger.atError()
                    .setMessage("Host resolution failed for WHOIS server: {}")
                    .addArgument(whoisServer)
                    .setCause(e)
                    .log();
            throw new WhoisQueryException("Host resolution failed for '" + whoisServer + "'", e);
        } catch (IOException e) {
            logger.atError()
                    .setMessage("Connection failed to WHOIS server: {}")
                    .addArgument(whoisServer)
                    .setCause(e)
                    .log();
            throw new WhoisQueryException("Connection failed to WHOIS server", e);
        } finally {
            try {
                logger.atDebug().log("Disconnecting from WHOIS server: {}", whoisServer);
                whoisClient.disconnect();
            } catch (IOException e) {
                logger.atError()
                        .setMessage("Failed to disconnect from WHOIS server: {}")
                        .addArgument(whoisServer)
                        .setCause(e)
                        .log();
                throw new WhoisQueryException("Failed to disconnect from WHOIS server", e);
            }
        }
    }
}
