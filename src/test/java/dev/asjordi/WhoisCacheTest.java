package dev.asjordi;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class WhoisCacheTest {

    private static final Logger logger = LoggerFactory.getLogger(WhoisCacheTest.class);
    private static WhoisCache whoisCache;

    @BeforeAll
    static void setUp() {
        logger.atTrace().log("Setting up WhoisCache instance for tests");
        whoisCache = new WhoisCache();
    }

    @Test
    void testCommonTlds() {
        logger.atTrace().log("Testing common TLDs");
        var comServer = whoisCache.getWhoisServer(".com");
        logger.atTrace().log("Retrieved server for .com: {}", comServer.orElse("Not Found"));
        assertTrue(comServer.isPresent());
        assertEquals("whois.verisign-grs.com", comServer.get());

        var netServer = whoisCache.getWhoisServer(".net");
        logger.atTrace().log("Retrieved server for .net: {}", netServer.orElse("Not Found"));
        assertTrue(netServer.isPresent());
        assertEquals("whois.verisign-grs.com", netServer.get());

        var orgServer = whoisCache.getWhoisServer(".org");
        logger.atTrace().log("Retrieved server for .org: {}", orgServer.orElse("Not Found"));
        assertTrue(orgServer.isPresent());
        assertEquals("whois.pir.org", orgServer.get());

        var ioServer = whoisCache.getWhoisServer(".io");
        logger.atTrace().log("Retrieved server for .io: {}", ioServer.orElse("Not Found"));
        assertTrue(ioServer.isPresent());
        assertEquals("whois.nic.io", ioServer.get());

        var coServer = whoisCache.getWhoisServer(".co");
        logger.atTrace().log("Retrieved server for .co: {}", coServer.orElse("Not Found"));
        assertTrue(coServer.isPresent());
        assertEquals("whois.nic.co", coServer.get());
    }

    @Test
    void testAdditionalTlds() {
        logger.atTrace().log("Testing additional TLDs");
        var appServer = whoisCache.getWhoisServer(".app");
        logger.atTrace().log("Retrieved server for .app: {}", appServer.orElse("Not Found"));
        assertTrue(appServer.isPresent());
        assertEquals("whois.nic.google", appServer.get());

        var techServer = whoisCache.getWhoisServer(".tech");
        logger.atTrace().log("Retrieved server for .tech: {}", techServer.orElse("Not Found"));
        assertTrue(techServer.isPresent());
        assertEquals("whois.nic.tech", techServer.get());

        var xyzServer = whoisCache.getWhoisServer(".xyz");
        logger.atTrace().log("Retrieved server for .xyz: {}", xyzServer.orElse("Not Found"));
        assertTrue(xyzServer.isPresent());
        assertEquals("whois.nic.xyz", xyzServer.get());

        var devServer = whoisCache.getWhoisServer(".dev");
        logger.atTrace().log("Retrieved server for .dev: {}", devServer.orElse("Not Found"));
        assertTrue(devServer.isPresent());
        assertEquals("whois.nic.google", devServer.get());

        var academyServer = whoisCache.getWhoisServer(".academy");
        logger.atTrace().log("Retrieved server for .academy: {}", academyServer.orElse("Not Found"));
        assertTrue(academyServer.isPresent());
        assertEquals("whois.donuts.co", academyServer.get());

        var blogServer = whoisCache.getWhoisServer(".blog");
        logger.atTrace().log("Retrieved server for .blog: {}", blogServer.orElse("Not Found"));
        assertTrue(blogServer.isPresent());
        assertEquals("whois.nic.blog", blogServer.get());

        var clubServer = whoisCache.getWhoisServer(".club");
        logger.atTrace().log("Retrieved server for .club: {}", clubServer.orElse("Not Found"));
        assertTrue(clubServer.isPresent());
        assertEquals("whois.nic.club", clubServer.get());
    }

    @Test
    void testNonExistentTlds() {
        logger.atTrace().log("Testing non-existent TLDs");
        var nonexistentServer = whoisCache.getWhoisServer(".nonexistent");
        logger.atTrace().log("Retrieved server for .nonexistent: {}", nonexistentServer.orElse("Not Found"));
        assertTrue(nonexistentServer.isEmpty());

        var invalidServer = whoisCache.getWhoisServer(".invalid");
        logger.atTrace().log("Retrieved server for .invalid: {}", invalidServer.orElse("Not Found"));
        assertTrue(invalidServer.isEmpty());

        var test123Server = whoisCache.getWhoisServer(".test123");
        logger.atTrace().log("Retrieved server for .test123: {}", test123Server.orElse("Not Found"));
        assertTrue(test123Server.isEmpty());
    }

    @Test
    void testCaseSensitivity() {
        logger.atTrace().log("Testing case sensitivity for TLDs");
        var comServer = whoisCache.getWhoisServer(".COM");
        logger.atTrace().log("Retrieved server for .COM: {}", comServer.orElse("Not Found"));
        assertTrue(comServer.isPresent());
        assertEquals("whois.verisign-grs.com", comServer.get());

        var orgServer = whoisCache.getWhoisServer(".ORG");
        logger.atTrace().log("Retrieved server for .ORG: {}", orgServer.orElse("Not Found"));
        assertTrue(orgServer.isPresent());
        assertEquals("whois.pir.org", orgServer.get());
    }

    @Test
    void testInvalidInputs() {
        logger.atTrace().log("Testing invalid inputs");
        var nullServer = whoisCache.getWhoisServer(null);
        logger.atTrace().log("Retrieved server for null input: {}", nullServer.orElse("Not Found"));
        assertTrue(nullServer.isEmpty());

        var emptyServer = whoisCache.getWhoisServer("");
        logger.atTrace().log("Retrieved server for empty input: {}", emptyServer.orElse("Not Found"));
        assertTrue(emptyServer.isEmpty());

        var comServer = whoisCache.getWhoisServer("com");
        logger.atTrace().log("Retrieved server for 'com' (missing dot): {}", comServer.orElse("Not Found"));
        assertTrue(comServer.isEmpty());
    }

    @Test
    void testTldFormats() {
        logger.atTrace().log("Testing TLD formats");
        var comWithDotServer = whoisCache.getWhoisServer(".com");
        logger.atTrace().log("Retrieved server for .com: {}", comWithDotServer.orElse("Not Found"));
        assertTrue(comWithDotServer.isPresent());
        assertEquals("whois.verisign-grs.com", comWithDotServer.get());

        var comWithoutDotServer = whoisCache.getWhoisServer("com");
        logger.atTrace().log("Retrieved server for 'com' (missing dot): {}", comWithoutDotServer.orElse("Not Found"));
        assertTrue(comWithoutDotServer.isEmpty());
    }

    @Test
    void testCountryCodeTlds() {
        logger.atTrace().log("Testing country code TLDs");
        var ukServer = whoisCache.getWhoisServer(".uk");
        logger.atTrace().log("Retrieved server for .uk: {}", ukServer.orElse("Not Found"));
        assertTrue(ukServer.isPresent());
        assertEquals("whois.nic.uk", ukServer.get());

        var caServer = whoisCache.getWhoisServer(".ca");
        logger.atTrace().log("Retrieved server for .ca: {}", caServer.orElse("Not Found"));
        assertTrue(caServer.isPresent());
        assertEquals("whois.cira.ca", caServer.get());

        var deServer = whoisCache.getWhoisServer(".de");
        logger.atTrace().log("Retrieved server for .de: {}", deServer.orElse("Not Found"));
        assertTrue(deServer.isPresent());
        assertEquals("whois.denic.de", deServer.get());

        var frServer = whoisCache.getWhoisServer(".fr");
        logger.atTrace().log("Retrieved server for .fr: {}", frServer.orElse("Not Found"));
        assertTrue(frServer.isPresent());
        assertEquals("whois.nic.fr", frServer.get());

        var ptServer = whoisCache.getWhoisServer(".pt");
        logger.atTrace().log("Retrieved server for .pt: {}", ptServer.orElse("Not Found"));
        assertTrue(ptServer.isPresent());
        assertEquals("whois.dns.pt", ptServer.get());
    }

    @Test
    void testNewGtlds() {
        logger.atTrace().log("Testing new gTLDs");
        var appServer = whoisCache.getWhoisServer(".app");
        logger.atTrace().log("Retrieved server for .app: {}", appServer.orElse("Not Found"));
        assertTrue(appServer.isPresent());
        assertEquals("whois.nic.google", appServer.get());

        var devServer = whoisCache.getWhoisServer(".dev");
        logger.atTrace().log("Retrieved server for .dev: {}", devServer.orElse("Not Found"));
        assertTrue(devServer.isPresent());
        assertEquals("whois.nic.google", devServer.get());

        var pageServer = whoisCache.getWhoisServer(".page");
        logger.atTrace().log("Retrieved server for .page: {}", pageServer.orElse("Not Found"));
        assertTrue(pageServer.isPresent());
        assertEquals("whois.nic.google", pageServer.get());

        var howServer = whoisCache.getWhoisServer(".how");
        logger.atTrace().log("Retrieved server for .how: {}", howServer.orElse("Not Found"));
        assertTrue(howServer.isPresent());
        assertEquals("whois.nic.google", howServer.get());
    }
}
