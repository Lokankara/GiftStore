package com.store.gift.exception;
/**
 * Exception thrown when a user already exists.
 */
public class UserAlreadyExistsException extends RuntimeException {
    /**
     * Constructs a new UserAlreadyExistsException
     * with the specified error message.
     *
     * @param message the error message
     */
    public UserAlreadyExistsException(
            final String message) {
        super(message);
    }
}
