package dev.asjordi;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DomainValidatorUtilTest {

    @Test
    void testValidDomain() {
        assertTrue(DomainValidatorUtil.isValidDomain("example.com"));
        assertTrue(DomainValidatorUtil.isValidDomain("sub.example.com"));
        assertTrue(DomainValidatorUtil.isValidDomain("example.co.uk"));
    }

    @Test
    void testInvalidDomain() {
        assertFalse(DomainValidatorUtil.isValidDomain("example..com"));
        assertFalse(DomainValidatorUtil.isValidDomain("example_com"));
        assertFalse(DomainValidatorUtil.isValidDomain("example"));
        assertFalse(DomainValidatorUtil.isValidDomain("example.invalidtld"));
        assertFalse(DomainValidatorUtil.isValidDomain("web-site"));
    }

    @Test
    void testNullDomain() {
        assertFalse(DomainValidatorUtil.isValidDomain(null));
    }

    @Test
    void testBlankDomain() {
        assertFalse(DomainValidatorUtil.isValidDomain(""));
        assertFalse(DomainValidatorUtil.isValidDomain("   "));
    }

    @Test
    void testDomainWithSpaces() {
        assertTrue(DomainValidatorUtil.isValidDomain(" example.com "));
        assertTrue(DomainValidatorUtil.isValidDomain("example .com"));
    }

    @Test
    void testDomainWithSpecialCharacters() {
        assertFalse(DomainValidatorUtil.isValidDomain("example!com"));
        assertFalse(DomainValidatorUtil.isValidDomain("example@com"));
    }

    @Test
    void testInternationalizedDomain() {
        assertTrue(DomainValidatorUtil.isValidDomain("उदाहरण.भारत"));
        assertTrue(DomainValidatorUtil.isValidDomain("xn--p1b6ci4b4b3a.xn--h2brj9c"));
        assertFalse(DomainValidatorUtil.isValidDomain("उदाहरण..भारत"));
        assertFalse(DomainValidatorUtil.isValidDomain("उदाहरण@भारत"));
    }

    @Test
    void testLocalDomains() {
        assertFalse(DomainValidatorUtil.isValidDomain("localhost"));
        assertFalse(DomainValidatorUtil.isValidDomain("127.0.0.1"));
        assertFalse(DomainValidatorUtil.isValidDomain("::1"));
    }

    @Test
    void testDomainWithInvalidHyphens() {
        assertFalse(DomainValidatorUtil.isValidDomain("-example.com"));
        assertFalse(DomainValidatorUtil.isValidDomain("example-.com"));
        assertFalse(DomainValidatorUtil.isValidDomain("sub.-example.com"));
    }

    @Test
    void testDomainWithMultipleSubdomains() {
        assertTrue(DomainValidatorUtil.isValidDomain("a.b.c.d.example.com"));
        assertTrue(DomainValidatorUtil.isValidDomain("deep.sub.domain.example.com"));
    }

    @Test
    void testDomainWithExtremeLengths() {
        String longDomain = "a".repeat(63) + ".com";
        assertTrue(DomainValidatorUtil.isValidDomain(longDomain));

        String tooLongDomain = "a".repeat(64) + ".com";
        assertFalse(DomainValidatorUtil.isValidDomain(tooLongDomain));

        String maxLengthDomain = "a".repeat(63) + "." + "b".repeat(63) + "." + "c".repeat(63) + ".com";
        assertTrue(DomainValidatorUtil.isValidDomain(maxLengthDomain));

        String overMaxLengthDomain = "a".repeat(63) + "." + "b".repeat(63) + "." + "c".repeat(63) + "." + "d".repeat(64) + ".com";
        assertFalse(DomainValidatorUtil.isValidDomain(overMaxLengthDomain));
    }
}
