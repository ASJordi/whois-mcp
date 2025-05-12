package dev.asjordi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class WhoisCache {

    private static final Logger logger = LoggerFactory.getLogger(WhoisCache.class);
    private final Map<String, String> cache;

    public WhoisCache() {
        cache = new HashMap<>();
        initializeCache();
    }

    private void initializeCache() {
        logger.atInfo().log("Initializing WHOIS cache...");
        Properties properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("whois-servers.properties")) {
            if (input != null) {
                logger.atDebug().log("Loading WHOIS servers from properties file...");
                properties.load(input);

                for (String key : properties.stringPropertyNames()) {
                    cache.put(key, properties.getProperty(key));
                    logger.atDebug().log("Loaded WHOIS server: {} -> {}", key, properties.getProperty(key));
                }
            } else {
                logger.atWarn().log("WHOIS properties file not found. Loading default servers...");
                loadDefaultServers();
            }
        } catch (IOException e) {
            logger.atError()
                    .setMessage("Error loading WHOIS properties file")
                    .setCause(e)
                    .log();
            logger.atInfo().log("Falling back to default WHOIS servers...");
            loadDefaultServers();
        }
    }

    private void loadDefaultServers() {
        logger.atInfo().log("Loading default WHOIS servers...");
        cache.put(".com", "whois.verisign-grs.com");
        cache.put(".net", "whois.verisign-grs.com");
        cache.put(".org", "whois.pir.org");
        cache.put(".io", "whois.nic.io");
        cache.put(".co", "whois.nic.co");
        logger.atDebug().log("Default WHOIS servers loaded.");
    }

    public Optional<String> getWhoisServer(String domain) {
        logger.atDebug().log("Retrieving WHOIS server for domain: {}", domain);

        if (domain == null || domain.isBlank() || !domain.startsWith(".")) {
            logger.atWarn().log("Invalid domain extension: {}", domain);
            return Optional.empty();
        }

        Optional<String> server = Optional.ofNullable(cache.get(domain.toLowerCase()));
        if (server.isPresent()) {
            logger.atInfo().log("Found WHOIS server for {}: {}", domain, server.get());
        } else {
            logger.atWarn().log("No WHOIS server found for domain: {}", domain);
        }

        return server;
    }
}
