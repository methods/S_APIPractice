package com.example.HearingsDemo.exception;

/**
 * Custom exception to be thrown when a requested database resource
 * (like a Tracking Status) does not exist.
 */

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Recommended: Add a constructor that accepts a cause for better stack traces
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}