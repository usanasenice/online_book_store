package com.iliamalafeev.bookstore.bookstore_backend.utils.exceptions;

import org.springframework.http.HttpStatus;

public class ValidationException extends RuntimeException {
    private final HttpStatus httpStatus;

    public ValidationException(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public ValidationException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
} 