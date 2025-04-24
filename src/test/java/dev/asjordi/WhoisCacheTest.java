package dev.asjordi;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WhoisCacheTest {

    private static WhoisCache whoisCache;

    @BeforeAll
    static void setUp() {
        whoisCache = new WhoisCache();
    }

    @Test
    void testCommonTlds() {
        var comServer = whoisCache.getWhoisServer(".com");
        assertTrue(comServer.isPresent());
        assertEquals("whois.verisign-grs.com", comServer.get());

        var netServer = whoisCache.getWhoisServer(".net");
        assertTrue(netServer.isPresent());
        assertEquals("whois.verisign-grs.com", netServer.get());

        var orgServer = whoisCache.getWhoisServer(".org");
        assertTrue(orgServer.isPresent());
        assertEquals("whois.pir.org", orgServer.get());

        var ioServer = whoisCache.getWhoisServer(".io");
        assertTrue(ioServer.isPresent());
        assertEquals("whois.nic.io", ioServer.get());

        var coServer = whoisCache.getWhoisServer(".co");
        assertTrue(coServer.isPresent());
        assertEquals("whois.nic.co", coServer.get());
    }

    @Test
    void testAdditionalTlds() {
        var appServer = whoisCache.getWhoisServer(".app");
        assertTrue(appServer.isPresent());
        assertEquals("whois.nic.google", appServer.get());

        var techServer = whoisCache.getWhoisServer(".tech");
        assertTrue(techServer.isPresent());
        assertEquals("whois.nic.tech", techServer.get());

        var xyzServer = whoisCache.getWhoisServer(".xyz");
        assertTrue(xyzServer.isPresent());
        assertEquals("whois.nic.xyz", xyzServer.get());

        var devServer = whoisCache.getWhoisServer(".dev");
        assertTrue(devServer.isPresent());
        assertEquals("whois.nic.google", devServer.get());

        var academyServer = whoisCache.getWhoisServer(".academy");
        assertTrue(academyServer.isPresent());
        assertEquals("whois.donuts.co", academyServer.get());

        var blogServer = whoisCache.getWhoisServer(".blog");
        assertTrue(blogServer.isPresent());
        assertEquals("whois.nic.blog", blogServer.get());

        var clubServer = whoisCache.getWhoisServer(".club");
        assertTrue(clubServer.isPresent());
        assertEquals("whois.nic.club", clubServer.get());
    }

    @Test
    void testNonExistentTlds() {
        var nonexistentServer = whoisCache.getWhoisServer(".nonexistent");
        assertTrue(nonexistentServer.isEmpty());

        var invalidServer = whoisCache.getWhoisServer(".invalid");
        assertTrue(invalidServer.isEmpty());

        var test123Server = whoisCache.getWhoisServer(".test123");
        assertTrue(test123Server.isEmpty());
    }

    @Test
    void testCaseSensitivity() {
        var comServer = whoisCache.getWhoisServer(".COM");
        assertTrue(comServer.isPresent());
        assertEquals("whois.verisign-grs.com", comServer.get());

        var orgServer = whoisCache.getWhoisServer(".ORG");
        assertTrue(orgServer.isPresent());
        assertEquals("whois.pir.org", orgServer.get());
    }

    @Test
    void testInvalidInputs() {
        var nullServer = whoisCache.getWhoisServer(null);
        assertTrue(nullServer.isEmpty());

        var emptyServer = whoisCache.getWhoisServer("");
        assertTrue(emptyServer.isEmpty());

        var comServer = whoisCache.getWhoisServer("com");
        assertTrue(comServer.isEmpty());
    }

    @Test
    void testTldFormats() {
        var comWithDotServer = whoisCache.getWhoisServer(".com");
        assertTrue(comWithDotServer.isPresent());
        assertEquals("whois.verisign-grs.com", comWithDotServer.get());

        var comWithoutDotServer = whoisCache.getWhoisServer("com");
        assertTrue(comWithoutDotServer.isEmpty());
    }

    @Test
    void testCountryCodeTlds() {
        var ukServer = whoisCache.getWhoisServer(".uk");
        assertTrue(ukServer.isPresent());
        assertEquals("whois.nic.uk", ukServer.get());

        var caServer = whoisCache.getWhoisServer(".ca");
        assertTrue(caServer.isPresent());
        assertEquals("whois.cira.ca", caServer.get());

        var deServer = whoisCache.getWhoisServer(".de");
        assertTrue(deServer.isPresent());
        assertEquals("whois.denic.de", deServer.get());

        var frServer = whoisCache.getWhoisServer(".fr");
        assertTrue(frServer.isPresent());
        assertEquals("whois.nic.fr", frServer.get());

        var ptServer = whoisCache.getWhoisServer(".pt");
        assertTrue(ptServer.isPresent());
        assertEquals("whois.dns.pt", ptServer.get());
    }

    @Test
    void testNewGtlds() {
        var appServer = whoisCache.getWhoisServer(".app");
        assertTrue(appServer.isPresent());
        assertEquals("whois.nic.google", appServer.get());

        var devServer = whoisCache.getWhoisServer(".dev");
        assertTrue(devServer.isPresent());
        assertEquals("whois.nic.google", devServer.get());

        var pageServer = whoisCache.getWhoisServer(".page");
        assertTrue(pageServer.isPresent());
        assertEquals("whois.nic.google", pageServer.get());

        var howServer = whoisCache.getWhoisServer(".how");
        assertTrue(howServer.isPresent());
        assertEquals("whois.nic.google", howServer.get());
    }
}
