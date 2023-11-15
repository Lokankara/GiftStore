package com.store.gift.exception;
/**
 * Exception thrown when an order is not found.
 */
public class OrderNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code OrderNotFoundException}
     * with the specified detail message.
     *
     * @param message the detail message
     */
    public OrderNotFoundException(
            final String message) {
        super(message);
    }
}
