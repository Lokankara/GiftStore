package com.store.gift.exception;


import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceNotFoundExceptionTest {

    @Test
    void constructor() {
        String httpMethod = "GET";
        String url = "/api";
        HttpHeaders httpHeaders = new HttpHeaders();
        ResourceNotFoundException exception =
                new ResourceNotFoundException(httpMethod, url, httpHeaders);
        assertEquals(httpMethod, exception.getHttpMethod());
        assertEquals(url, exception.getRequestURL());
    }

    @Test
    void status() {
        ResourceNotFoundException exception = new ResourceNotFoundException(null, null, null);
        HttpStatus status = exception.getClass().getAnnotation(ResponseStatus.class).value();
        assertEquals(HttpStatus.NOT_FOUND, status);
    }
}
