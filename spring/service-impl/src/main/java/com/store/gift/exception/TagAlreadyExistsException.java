package com.store.gift.exception;

/**
 * Exception thrown when a tag already exists.
 */
public class TagAlreadyExistsException extends RuntimeException {
    /**
     * Constructs a new {@code TagAlreadyExistsException} with the specified detail message.
     *
     * @param message the detail message
     */
    public TagAlreadyExistsException(final String message) {
        super(message);
    }
}
