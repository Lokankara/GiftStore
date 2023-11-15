package com.store.gift.exception;

/**
 * The RoleNotFoundException is an exception
 * that is thrown when a role is not found.
 * <p>
 * It extends the RuntimeException class.
 */
public class RoleNotFoundException extends RuntimeException {
    /**
     * Constructs a new RoleNotFoundException with the specified error message.
     *
     * @param message the error message
     */
    public RoleNotFoundException(
            final String message) {
        super(message);
    }
}
