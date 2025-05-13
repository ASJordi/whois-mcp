package dev.asjordi;

import dev.asjordi.exceptions.DomainValidationException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class DomainSanitizerTest {

    private static final Logger logger = LoggerFactory.getLogger(DomainSanitizerTest.class);

    @Test
    void testSanitizeRemovesWhitespace() {
        String input = "  example.com  ";
        logger.atTrace().log("Testing sanitize with input: '{}'", input);
        String expected = "example.com";
        String result = DomainSanitizer.sanitize(input);
        assertEquals(expected, result);
    }

    @Test
    void testSanitizeRemovesWWW() {
        String input = "www.example.com";
        logger.atTrace().log("Testing sanitize with input: '{}'", input);
        String expected = "example.com";
        String result = DomainSanitizer.sanitize(input);
        assertEquals(expected, result);
    }

    @Test
    void testSanitizeConvertsToLowerCase() {
        String input = "EXAMPLE.COM";
        logger.atTrace().log("Testing sanitize with input: '{}'", input);
        String expected = "example.com";
        String result = DomainSanitizer.sanitize(input);
        assertEquals(expected, result);
    }

    @Test
    void testSanitizeHandlesNullInput() {
        String input = null;
        logger.atTrace().log("Testing sanitize with null input");
        assertThrows(DomainValidationException.class, () -> {
            try {
                DomainSanitizer.sanitize(input);
            } catch (DomainValidationException e) {
                logger.atTrace()
                        .setMessage("Domain validation exception")
                        .setCause(e)
                        .log();
                throw e;
            }
        });
    }

    @Test
    void testSanitizeHandlesEmptyInput() {
        String input = "";
        logger.atTrace().log("Testing sanitize with empty input");
        assertThrows(DomainValidationException.class, () -> {
            try {
                DomainSanitizer.sanitize(input);
            } catch (DomainValidationException e) {
                logger.atTrace()
                        .setMessage("Domain validation exception")
                        .setCause(e)
                        .log();
                throw e;
            }
        });
    }

    @Test
    void testSanitizeHandlesOnlyWhitespaceInput() {
        String input = "   ";
        logger.atTrace().log("Testing sanitize with whitespace-only input");
        assertThrows(DomainValidationException.class, () -> {
            try {
                DomainSanitizer.sanitize(input);
            } catch (DomainValidationException e) {
                logger.atTrace()
                        .setMessage("Domain validation exception")
                        .setCause(e)
                        .log();
                throw e;
            }
        });
    }

    @Test
    void testSanitizeComplexInput() {
        String input = "  www.Example.COM  ";
        logger.atTrace().log("Testing sanitize with complex input: '{}'", input);
        String expected = "example.com";
        String result = DomainSanitizer.sanitize(input);
        assertEquals(expected, result);
    }

    @Test
    void testSanitizeRemovesHttpPrefix() {
        String input = "http://example.com";
        logger.atTrace().log("Testing sanitize with input: '{}'", input);
        String expected = "example.com";
        String result = DomainSanitizer.sanitize(input);
        assertEquals(expected, result);
    }

    @Test
    void testSanitizeRemovesHttpsPrefix() {
        String input = "https://example.com";
        logger.atTrace().log("Testing sanitize with input: '{}'", input);
        String expected = "example.com";
        String result = DomainSanitizer.sanitize(input);
        assertEquals(expected, result);
    }

    @Test
    void testSanitizeRemovesHttpsAndWWW() {
        String input = "https://www.example.com";
        logger.atTrace().log("Testing sanitize with input: '{}'", input);
        String expected = "example.com";
        String result = DomainSanitizer.sanitize(input);
        assertEquals(expected, result);
    }

    @Test
    void testSanitizeRemovesHttpAndWWW() {
        String input = "http://www.example.com";
        logger.atTrace().log("Testing sanitize with input: '{}'", input);
        String expected = "example.com";
        String result = DomainSanitizer.sanitize(input);
        assertEquals(expected, result);
    }
}
