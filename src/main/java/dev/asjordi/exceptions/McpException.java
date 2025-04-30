package dev.asjordi.exceptions;

/**
 * Base exception class for the MCP project.
 * <p>
 * All custom exceptions in the project should extend this class.
 * </p>
 */
public class McpException extends RuntimeException {

    /**
     * Constructs a new McpException with the specified detail message.
     *
     * @param message the detail message
     */
    public McpException(String message) {
        super(message);
    }

    /**
     * Constructs a new McpException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public McpException(String message, Throwable cause) {
        super(message, cause);
    }
}
