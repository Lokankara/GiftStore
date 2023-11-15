package com.store.gift.handler;

import com.store.gift.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class ErrorHandlerControllerTest {

    ErrorHandlerController exceptionHandler = new ErrorHandlerController();

    ResponseMessage message = new ResponseMessage(HttpStatus.OK, "message");

    @Test
    void conflictShouldReturnConflictResponse() {
        DataIntegrityViolationException exception =
                new DataIntegrityViolationException("message");
        ResponseEntity<Object> response = exceptionHandler.conflict(exception);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void handleNoSuchElementFoundExceptionShouldReturnNotFoundResponse() {
        ResourceNotFoundException exception =
                new ResourceNotFoundException("GET", "/test", new HttpHeaders());
        ResponseEntity<Object> response = exceptionHandler
                .handleNoSuchElementFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testHashCode() {
        ResponseMessage message2 = new ResponseMessage(HttpStatus.OK, "message2");
        assertNotEquals(message.hashCode(), message2.hashCode());
    }

    @Test
    void testEquals() {
        ResponseMessage message2 = new ResponseMessage(HttpStatus.OK, "message2");
        assertNotEquals(message, message2);
    }

    @Test
    void testToString() {
        assertEquals(
                "ResponseMessage(statusCode=200 OK, errorMessage=message)",
                message.toString());
    }

    @Test
    void testSetStatusCode() {
        message.setStatusCode(HttpStatus.NOT_FOUND);
        assertEquals(HttpStatus.NOT_FOUND, message.getStatusCode());
    }

    @Test
    void testSetErrorMessage() {
        message.setErrorMessage("new message");
        assertEquals("new message", message.getErrorMessage());
    }
}
