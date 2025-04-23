package dev.asjordi;

/**
 * Utility class for sanitizing domain names.
 * <p>
 * This class provides methods to clean and standardize domain names by removing
 * unnecessary characters, trimming whitespace, and ensuring proper formatting.
 * It is particularly useful for preparing domain names for further processing
 * or validation.
 * </p>
 */
public class DomainSanitizer {

    /**
     * Sanitizes a domain name by removing unnecessary characters and ensuring proper formatting.
     * 
     * @param domain The input domain name.
     * @return A sanitized domain name.
     */
    public static String sanitize(String domain) {
        if (domain == null || domain.isBlank()) throw new IllegalArgumentException("Domain cannot be null or empty");
        domain = domain.replaceAll("\\s", "").trim();
        if (domain.startsWith("www.")) domain = domain.substring(4);
        return domain.toLowerCase();
    }
}

