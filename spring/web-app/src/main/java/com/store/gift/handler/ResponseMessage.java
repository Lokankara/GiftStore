package com.store.gift.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * A class representing a response message.
 */
@Data
@Builder
@AllArgsConstructor
public class ResponseMessage {
    /**
     * The HTTP status code of the response.
     */
    private HttpStatus statusCode;
    /**
     * The error message associated with the response.
     */
    private String errorMessage;
}
