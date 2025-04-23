package dev.asjordi;

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
        assertThrows(IllegalArgumentException.class, () -> DomainSanitizer.sanitize(null));
    }

    @Test
    void testSanitizeHandlesEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> DomainSanitizer.sanitize(""));
    }

    @Test
    void testSanitizeHandlesOnlyWhitespaceInput() {
        assertThrows(IllegalArgumentException.class, () -> DomainSanitizer.sanitize("   "));
    }

    @Test
    void testSanitizeComplexInput() {
        String input = "  www.Example.COM  ";
        String expected = "example.com";
        assertEquals(expected, DomainSanitizer.sanitize(input));
    }
}
