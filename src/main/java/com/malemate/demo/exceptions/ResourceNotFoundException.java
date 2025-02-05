package com.malemate.demo.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Requested resource not found.";

    public ResourceNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
