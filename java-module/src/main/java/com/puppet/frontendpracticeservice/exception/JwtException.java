package com.puppet.frontendpracticeservice.exception;

public class JwtException extends RuntimeException {
    public JwtException() {
    }

    public JwtException(String message) {
        super(message);
    }
}
