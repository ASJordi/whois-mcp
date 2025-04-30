package dev.asjordi;

import dev.asjordi.exceptions.DomainValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DomainSanitizerTest {

    @Test
    void testSanitizeRemovesWhitespace() {
        String input = "  example.com  ";
        String expected = "example.com";
        assertEquals(expected, DomainSanitizer.sanitize(input));
    }

    @Test
    void testSanitizeRemovesWWW() {
        String input = "www.example.com";
        String expected = "example.com";
        assertEquals(expected, DomainSanitizer.sanitize(input));
    }

    @Test
    void testSanitizeConvertsToLowerCase() {
        String input = "EXAMPLE.COM";
        String expected = "example.com";
        assertEquals(expected, DomainSanitizer.sanitize(input));
    }

    @Test
    void testSanitizeHandlesNullInput() {
        assertThrows(DomainValidationException.class, () -> DomainSanitizer.sanitize(null));
    }

    @Test
    void testSanitizeHandlesEmptyInput() {
        assertThrows(DomainValidationException.class, () -> DomainSanitizer.sanitize(""));
    }

    @Test
    void testSanitizeHandlesOnlyWhitespaceInput() {
        assertThrows(DomainValidationException.class, () -> DomainSanitizer.sanitize("   "));
    }

    @Test
    void testSanitizeComplexInput() {
        String input = "  www.Example.COM  ";
        String expected = "example.com";
        assertEquals(expected, DomainSanitizer.sanitize(input));
    }

    @Test
    void testSanitizeRemovesHttpPrefix() {
        String input = "http://example.com";
        String expected = "example.com";
        assertEquals(expected, DomainSanitizer.sanitize(input));
    }

    @Test
    void testSanitizeRemovesHttpsPrefix() {
        String input = "https://example.com";
        String expected = "example.com";
        assertEquals(expected, DomainSanitizer.sanitize(input));
    }

    @Test
    void testSanitizeRemovesHttpsAndWWW() {
        String input = "https://www.example.com";
        String expected = "example.com";
        assertEquals(expected, DomainSanitizer.sanitize(input));
    }

    @Test
    void testSanitizeRemovesHttpAndWWW() {
        String input = "http://www.example.com";
        String expected = "example.com";
        assertEquals(expected, DomainSanitizer.sanitize(input));
    }
}
