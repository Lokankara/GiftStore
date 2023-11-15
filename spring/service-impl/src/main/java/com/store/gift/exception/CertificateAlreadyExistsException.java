package com.store.gift.exception;

/**
 * Exception thrown when a certificate already exists.
 */
public class CertificateAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new CertificateAlreadyExistsException
     * with the specified detail message.
     *
     * @param message the detail message.
     */
    public CertificateAlreadyExistsException(
            final String message) {
        super(message);
    }
}
