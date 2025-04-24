package dev.asjordi;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * Provides a caching mechanism for WHOIS servers.
 * <p>
 * This class initializes a cache of WHOIS servers from a properties file or defaults
 * to predefined servers if the file is unavailable. The cache allows querying WHOIS
 * servers based on domain extensions.
 * </p>
 */
public class WhoisCache {

    private final Map<String, String> cache;

    /**
     * Creates a new instance of {@code WhoisCache} and initializes the cache.
     */
    public WhoisCache() {
        cache = new HashMap<>();
        initializeCache();
    }

    /**
     * Loads WHOIS servers into the cache.
     * 
     * This method attempts to load WHOIS servers from a properties file. If the file
     * is unavailable or an error occurs, it falls back to loading default WHOIS servers.
     */
    private void initializeCache() {
        Properties properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("whois-servers.properties")) {
            if (input != null) {
                properties.load(input);

                for (String key : properties.stringPropertyNames()) {
                    cache.put(key, properties.getProperty(key));
                }
            } else loadDefaultServers();
        } catch (IOException e) {
            loadDefaultServers();
        }
    }

    /**
     * Loads a predefined set of default WHOIS servers into the cache.
     */
    private void loadDefaultServers() {
        cache.put(".com", "whois.verisign-grs.com");
        cache.put(".net", "whois.verisign-grs.com");
        cache.put(".org", "whois.pir.org");
        cache.put(".io", "whois.nic.io");
        cache.put(".co", "whois.nic.co");
    }

    /**
     * Retrieves the WHOIS server for the specified domain extension.
     * 
     * @param domain the domain extension (e.g., ".com", ".net"). Must start with a dot.
     * @return an {@code Optional} containing the WHOIS server if found, or an empty {@code Optional} if not.
     */
    public Optional<String> getWhoisServer(String domain) {
        if (domain == null || domain.isBlank() || !domain.startsWith(".")) return Optional.empty();
        return Optional.ofNullable(cache.get(domain.toLowerCase()));
    }

}
