package dev.asjordi;

import org.apache.commons.validator.routines.DomainValidator;

/**
 * Utility class for validating domain names.
 * <p>
 * This class provides a method to validate domain names using the Apache Commons Validator library.
 * It ensures that the domain name is properly formatted and adheres to standard domain name rules.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 *     boolean isValid = DomainValidatorUtil.isValidDomain("example.com");
 *     System.out.println("Is valid: " + isValid); // Output: true
 * </pre>
 * </p>
 */
public class DomainValidatorUtil {

    /**
     * A private constructor to prevent instantiation.
     */
    private DomainValidatorUtil() { }

    /**
     * Validates if the given domain name is valid.
     *
     * @param domainName the domain name to validate
     * @return true if the domain name is valid, false otherwise
     */
    public static boolean isValidDomain(String domainName) {
        if (domainName == null || domainName.isBlank()) return false;

        domainName = DomainSanitizer.sanitize(domainName);
        DomainValidator validator = DomainValidator.getInstance();

        return validator.isValid(domainName);
    }

}
