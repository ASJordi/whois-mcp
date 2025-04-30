package dev.asjordi;

import dev.asjordi.exceptions.DomainValidationException;

public class DomainSanitizer {

    public static String sanitize(String domain) {
        if (domain == null || domain.isBlank()) throw new DomainValidationException("Domain cannot be null or empty");

        domain = domain.replaceAll("\\s", "").trim();

        if (domain.startsWith("http://")) domain = domain.substring(7);
        if (domain.startsWith("https://")) domain = domain.substring(8);

        if (domain.startsWith("www.")) domain = domain.substring(4);

        return domain.toLowerCase();
    }
}
