package dev.asjordi.exceptions;

/**
 * Exception thrown when a WHOIS query fails.
 */
public class WhoisQueryException extends McpException {

    /**
     * Constructs a new WhoisQueryException with the specified detail message.
     *
     * @param message the detail message
     */
    public WhoisQueryException(String message) {
        super(message);
    }

    /**
     * Constructs a new WhoisQueryException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public WhoisQueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
