package com.store.gift.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Exception thrown when a requested resource is not found.
 */
@ResponseStatus(NOT_FOUND)
public class ResourceNotFoundException extends NoHandlerFoundException {
    /**
     * Constructs a new {@code ResourceNotFoundException}
     * with the specified HTTP method, URL, and HTTP headers.
     *
     * @param httpMethod  the HTTP method of the request
     * @param url         the URL of the request
     * @param httpHeaders the HTTP headers of the request
     */
    public ResourceNotFoundException(
            final String httpMethod,
            final String url,
            final HttpHeaders httpHeaders) {
        super(httpMethod, url, httpHeaders);
    }
}
