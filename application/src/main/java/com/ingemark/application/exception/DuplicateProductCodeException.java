package com.ingemark.application.exception;

public class DuplicateProductCodeException extends RuntimeException {

    public DuplicateProductCodeException(String message) {
        super(message);
    }
}
