package com.store.gift.handler;

import com.store.gift.exception.CertificateAlreadyExistsException;
import com.store.gift.exception.CertificateNotFoundException;
import com.store.gift.exception.ResourceNotFoundException;
import com.store.gift.exception.TagAlreadyExistsException;
import com.store.gift.exception.TagNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Controller advice for handling exceptions
 * and generating appropriate error responses.
 */
@RestControllerAdvice
public class ErrorHandlerController extends ResponseEntityExceptionHandler {

    /**
     * Handles DataIntegrityViolationException
     * and generates a conflict response.
     *
     * @param exception the DataIntegrityViolationException to handle
     * @return the ResponseEntity with the conflict response
     */
    @ResponseStatus(value = CONFLICT, reason = "Data integrity violation")
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> conflict(
            final DataIntegrityViolationException exception) {
        return buildErrorResponse(
                exception.getMessage(),
                CONFLICT
        );
    }

    /**
     * Handles ResourceNotFoundException
     * and generates a not found response.
     *
     * @param exception the ResourceNotFoundException to handle
     * @return the ResponseEntity with the not found response
     */
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNoSuchElementFoundException(
            final ResourceNotFoundException exception) {
        return buildErrorResponse(
                exception.getMessage(),
                NOT_FOUND
        );
    }

    /**
     * Handles CertificateNotFoundException
     * and TagNotFoundException and generates a not found response.
     *
     * @param exception the RuntimeException to handle
     * @return the ResponseEntity with the not found response
     */
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler({CertificateNotFoundException.class,
            TagNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFoundException(
            final RuntimeException exception) {
        return buildErrorResponse(
                exception.getMessage(),
                NOT_FOUND
        );
    }

    /**
     * Handles CertificateAlreadyExistsException
     * and TagAlreadyExistsException
     * and generates a bad request response.
     *
     * @param exception the RuntimeException to handle
     * @return the ResponseEntity with the bad request response
     */
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({CertificateAlreadyExistsException.class,
            TagAlreadyExistsException.class})
    public ResponseEntity<Object> handleEntityIsExistsException(
            final RuntimeException exception) {
        return buildErrorResponse(
                exception.getMessage(),
                BAD_REQUEST
        );
    }

    /**
     * Handles general exceptions
     * and generates an internal server error response.
     *
     * @param exception the Exception to handle
     * @return the ResponseEntity with the internal server error response
     */
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class, IllegalStateException.class})
    public ResponseEntity<Object> handleException(
            final Exception exception) {
        return buildErrorResponse(
                exception.getMessage(),
                INTERNAL_SERVER_ERROR
        );
    }

    /**
     * Handles uncaught RuntimeExceptions
     * and generates a method not allowed response.
     *
     * @param exception the RuntimeException to handle
     * @return the ResponseEntity
     * with the method not allowed response
     */
    @ResponseStatus(METHOD_NOT_ALLOWED)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleAllUncaughtException(
            final RuntimeException exception) {
        return buildErrorResponse(
                exception.getMessage(),
                METHOD_NOT_ALLOWED
        );
    }

    /**
     * Builds a response entity for error responses.
     *
     * @param messageError the error message
     * @param httpStatus   the HTTP status
     * @return the ResponseEntity object
     * containing the error response
     */
    private ResponseEntity<Object> buildErrorResponse(
            final String messageError,
            final HttpStatus httpStatus) {
        return ResponseEntity
                .status(httpStatus)
                .body(ResponseMessage.builder()
                        .errorMessage(messageError)
                        .statusCode(httpStatus)
                        .build());
    }
}
