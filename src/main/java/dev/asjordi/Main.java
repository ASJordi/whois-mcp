package dev.asjordi;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.asjordi.exceptions.DomainValidationException;
import dev.asjordi.exceptions.WhoisQueryException;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Main application class for the WHOIS MCP server.
 * <p>
 * This class initializes and starts a Model Context Protocol (MCP) server that 
 * provides WHOIS lookup functionality. The server uses standard input/output for
 * communication and exposes a tool for querying WHOIS information for domains.
 * </p>
 * <p>
 * The server implementation utilizes the MCP framework to handle requests and
 * responses in a standardized format, making it compatible with MCP clients.
 * </p>
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final WhoisService whoisService = new WhoisService();

    /**
     * The entry point of the application.
     * <p>
     * Initializes and starts the MCP server with the WHOIS tool specification.
     * The server communicates over standard input/output and provides domain
     * WHOIS lookup capabilities.
     * </p>
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        var transportProvider = new StdioServerTransportProvider(new ObjectMapper());
        var syncToolSpecification = getSyncToolSpecification();

        McpServer.sync(transportProvider)
                .serverInfo("whois-mcp-server", "0.0.1")
                .capabilities(McpSchema.ServerCapabilities.builder()
                        .tools(true)
                        .logging()
                        .build())
                .tools(syncToolSpecification)
                .build();

        logger.atInfo().log("Starting WHOIS server...");
    }

    /**
     * Creates and returns the specification for the WHOIS tool.
     * <p>
     * This method defines the schema and behavior of the WHOIS lookup tool,
     * including the expected input format (a domain name) and the processing
     * logic that performs the actual WHOIS query.
     * </p>
     * <p>
     * The tool validates the provided domain, performs the WHOIS query using
     * the WhoisService, and returns the results in a format compatible with
     * the MCP specification.
     * </p>
     *
     * @return A tool specification that can be registered with the MCP server
     */
    private static McpServerFeatures.SyncToolSpecification getSyncToolSpecification() {
        var schema = """
            {
              "type" : "object",
              "id" : "urn:jsonschema:WhoisRequest",
              "properties" : {
                "domain" : {
                  "type" : "string",
                  "description": "The domain to lookup WHOIS information for"
                }
              },
              "required": ["domain"]
            }
        """;

        return new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool(
                        "get_whois",
                        "Get WHOIS information for a domain",
                        schema
                ),
                (McpSyncServerExchange exchange, Map<String, Object> args) -> {

                    String domain = (String) args.get("domain");

                    try {
                        if (!DomainValidatorUtil.isValidDomain(domain)) {
                            throw new DomainValidationException("Invalid domain: " + domain);
                        }

                        var info = whoisService.performWhoisQuery(domain);

                        if (info.isPresent()) {
                            logger.atInfo().log("WHOIS information retrieved for domain: {}", domain);
                            List<McpSchema.Content> contents = new ArrayList<>();
                            contents.add(new McpSchema.TextContent(info.get()));

                            return new McpSchema.CallToolResult(contents, false);
                        } else {
                            logger.atInfo().log("No WHOIS information available for domain: {}", domain);
                            return new McpSchema.CallToolResult(
                                    List.of(new McpSchema.TextContent("No WHOIS information available for " + domain)),
                                    false
                            );
                        }
                    } catch (DomainValidationException | WhoisQueryException e) {
                        logger.atError()
                                .setMessage("Error processing domain: {}")
                                .addArgument(domain)
                                .setCause(e)
                                .log();
                        return new McpSchema.CallToolResult(
                                List.of(new McpSchema.TextContent("Error: " + e.getMessage())),
                                true
                        );
                    }
                }
        );
    }
}
