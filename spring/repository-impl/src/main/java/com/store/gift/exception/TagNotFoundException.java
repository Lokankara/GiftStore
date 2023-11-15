package com.store.gift.exception;

/**
 * Exception thrown when a tag is not found.
 */
public class TagNotFoundException extends RuntimeException {
    /**
     * Constructs a new {@code TagNotFoundException}
     * with the specified detail message.
     *
     * @param message the detail message
     */
    public TagNotFoundException(final String message) {
        super(message);
    }
}
