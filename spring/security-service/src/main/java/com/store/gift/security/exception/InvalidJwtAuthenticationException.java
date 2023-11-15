package com.store.gift.security.exception;

/**
 * Exception thrown when JWT authentication fails or is invalid.
 */
public class InvalidJwtAuthenticationException
        extends RuntimeException {
    /**
     * Constructs a new InvalidJwtAuthenticationException with the specified error message.
     *
     * @param message The error message.
     */
    public InvalidJwtAuthenticationException(
            final String message) {
        super(message);
    }
}
