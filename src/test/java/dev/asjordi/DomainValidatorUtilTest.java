package dev.asjordi;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class DomainValidatorUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(DomainValidatorUtilTest.class);

    @Test
    void testValidDomain() {
        logger.atTrace().log("Starting test for valid domains");
        assertTrue(DomainValidatorUtil.isValidDomain("example.com"));
        assertTrue(DomainValidatorUtil.isValidDomain("sub.example.com"));
        assertTrue(DomainValidatorUtil.isValidDomain("example.co.uk"));
    }

    @Test
    void testInvalidDomain() {
        logger.atTrace().log("Starting test for invalid domains");
        assertFalse(DomainValidatorUtil.isValidDomain("example..com"));
        assertFalse(DomainValidatorUtil.isValidDomain("example_com"));
        assertFalse(DomainValidatorUtil.isValidDomain("example"));
        assertFalse(DomainValidatorUtil.isValidDomain("example.invalidtld"));
        assertFalse(DomainValidatorUtil.isValidDomain("web-site"));
    }

    @Test
    void testNullDomain() {
        logger.atTrace().log("Starting test for null domain");
        assertFalse(DomainValidatorUtil.isValidDomain(null));
    }

    @Test
    void testBlankDomain() {
        logger.atTrace().log("Starting test for blank domain");
        assertFalse(DomainValidatorUtil.isValidDomain(""));
        assertFalse(DomainValidatorUtil.isValidDomain("   "));
    }

    @Test
    void testDomainWithSpaces() {
        logger.atTrace().log("Starting test for domain with spaces");
        assertTrue(DomainValidatorUtil.isValidDomain(" example.com "));
        assertTrue(DomainValidatorUtil.isValidDomain("example .com"));
    }

    @Test
    void testDomainWithSpecialCharacters() {
        logger.atTrace().log("Starting test for domain with special characters");
        assertFalse(DomainValidatorUtil.isValidDomain("example!com"));
        assertFalse(DomainValidatorUtil.isValidDomain("example@com"));
    }

    @Test
    void testInternationalizedDomain() {
        logger.atTrace().log("Starting test for internationalized domain");
        assertTrue(DomainValidatorUtil.isValidDomain("उदाहरण.भारत"));
        assertTrue(DomainValidatorUtil.isValidDomain("xn--p1b6ci4b4b3a.xn--h2brj9c"));
        assertFalse(DomainValidatorUtil.isValidDomain("उदाहरण..भारत"));
        assertFalse(DomainValidatorUtil.isValidDomain("उदाहरण@भारत"));
    }

    @Test
    void testLocalDomains() {
        logger.atTrace().log("Starting test for local domains");
        assertFalse(DomainValidatorUtil.isValidDomain("localhost"));
        assertFalse(DomainValidatorUtil.isValidDomain("127.0.0.1"));
        assertFalse(DomainValidatorUtil.isValidDomain("::1"));
    }

    @Test
    void testDomainWithInvalidHyphens() {
        logger.atTrace().log("Starting test for domain with invalid hyphens");
        assertFalse(DomainValidatorUtil.isValidDomain("-example.com"));
        assertFalse(DomainValidatorUtil.isValidDomain("example-.com"));
        assertFalse(DomainValidatorUtil.isValidDomain("sub.-example.com"));
    }

    @Test
    void testDomainWithMultipleSubdomains() {
        logger.atTrace().log("Starting test for domain with multiple subdomains");
        assertTrue(DomainValidatorUtil.isValidDomain("a.b.c.d.example.com"));
        assertTrue(DomainValidatorUtil.isValidDomain("deep.sub.domain.example.com"));
    }

    @Test
    void testDomainWithExtremeLengths() {
        logger.atTrace().log("Starting test for domain with extreme lengths");
        String longDomain = "a".repeat(63) + ".com";
        logger.atTrace().log("Testing long domain: '{}'", longDomain);
        assertTrue(DomainValidatorUtil.isValidDomain(longDomain));

        String tooLongDomain = "a".repeat(64) + ".com";
        logger.atTrace().log("Testing too long domain: '{}'", tooLongDomain);
        assertFalse(DomainValidatorUtil.isValidDomain(tooLongDomain));

        String maxLengthDomain = "a".repeat(63) + "." + "b".repeat(63) + "." + "c".repeat(63) + ".com";
        logger.atTrace().log("Testing max length domain: '{}'", maxLengthDomain);
        assertTrue(DomainValidatorUtil.isValidDomain(maxLengthDomain));

        String overMaxLengthDomain = "a".repeat(63) + "." + "b".repeat(63) + "." + "c".repeat(63) + "." + "d".repeat(64) + ".com";
        logger.atTrace().log("Testing over max length domain: '{}'", overMaxLengthDomain);
        assertFalse(DomainValidatorUtil.isValidDomain(overMaxLengthDomain));
    }
}
