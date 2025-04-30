package dev.asjordi.exceptions;

/**
 * Exception thrown when a domain validation error occurs.
 */
public class DomainValidationException extends McpException {

    /**
     * Constructs a new DomainValidationException with the specified detail message.
     *
     * @param message the detail message
     */
    public DomainValidationException(String message) {
        super(message);
    }
}
