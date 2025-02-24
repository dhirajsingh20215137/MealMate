package com.malemate.demo.exceptions;

public class BadRequestException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Bad request. Please check your input.";

    public BadRequestException() {
        super(DEFAULT_MESSAGE);
    }

    public BadRequestException(String message) {
        super(message);
    }
}
