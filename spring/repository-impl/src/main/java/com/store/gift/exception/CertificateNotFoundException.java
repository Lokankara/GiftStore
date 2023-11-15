package com.store.gift.exception;

/**
 * Exception thrown when a certificate is not found.
 */
public class CertificateNotFoundException extends RuntimeException {

    /**
     * Constructs a new CertificateNotFoundException.
     *
     * @param message The error message.
     */
    public CertificateNotFoundException(
            final String message) {
        super(message);
    }
}
