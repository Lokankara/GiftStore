package com.store.gift.exception;

/**
 * Exception thrown when a token is not found.
 */
public class TokenNotFoundException extends RuntimeException {
    /**
     * Constructs a new TokenNotFoundException with the specified error message.
     *
     * @param message the error message
     */
    public TokenNotFoundException(final String message) {
        super(message);
    }
}
