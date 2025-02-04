package com.malemate.demo.exceptions;

public class UnauthorizedException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Unauthorized access. Please log in or check your permissions.";

    public UnauthorizedException() {
        super(DEFAULT_MESSAGE);
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
