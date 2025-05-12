package dev.asjordi;

import org.apache.commons.validator.routines.DomainValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DomainValidatorUtil {

    private static final Logger logger = LoggerFactory.getLogger(DomainValidatorUtil.class);

    private DomainValidatorUtil() { }

    public static boolean isValidDomain(String domainName) {
        logger.atDebug().log("Validating domain: {}", domainName);

        if (domainName == null || domainName.isBlank()) {
            logger.atWarn().log("Domain is null or blank");
            return false;
        }

        domainName = DomainSanitizer.sanitize(domainName);
        logger.atDebug().log("Sanitized domain: {}", domainName);

        DomainValidator validator = DomainValidator.getInstance();
        boolean isValid = validator.isValid(domainName);

        if (isValid) logger.atInfo().log("Domain is valid: {}", domainName);
        else logger.atWarn().log("Domain is invalid: {}", domainName);

        return isValid;
    }
}
