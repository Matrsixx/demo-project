package com.example.demo.Exception;

import org.springframework.http.HttpStatus;

public class DefaultException extends RuntimeException {
    private final HttpStatus status;

    public DefaultException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
