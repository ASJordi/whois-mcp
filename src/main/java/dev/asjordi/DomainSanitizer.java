package dev.asjordi;

import dev.asjordi.exceptions.DomainValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DomainSanitizer {

    private static final Logger logger = LoggerFactory.getLogger(DomainSanitizer.class);

    public static String sanitize(String domain) {
        logger.atDebug().log("Sanitizing domain: {}", domain);

        if (domain == null || domain.isBlank()) {
            logger.atError().log("Domain is null or empty");
            throw new DomainValidationException("Domain cannot be null or empty");
        }

        domain = domain.replaceAll("\\s", "").trim();
        logger.atDebug().log("Domain after removing spaces: {}", domain);

        if (domain.startsWith("http://")) {
            domain = domain.substring(7);
            logger.atDebug().log("Domain after removing 'http://': {}", domain);
        }
        if (domain.startsWith("https://")) {
            domain = domain.substring(8);
            logger.atDebug().log("Domain after removing 'https://': {}", domain);
        }

        if (domain.startsWith("www.")) {
            domain = domain.substring(4);
            logger.atDebug().log("Domain after removing 'www.': {}", domain);
        }

        domain = domain.toLowerCase();
        logger.atDebug().log("Final sanitized domain: {}", domain);

        return domain;
    }
}
