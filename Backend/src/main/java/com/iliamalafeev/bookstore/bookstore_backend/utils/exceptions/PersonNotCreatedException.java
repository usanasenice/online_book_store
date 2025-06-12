package com.iliamalafeev.bookstore.bookstore_backend.utils.exceptions;

public class PersonNotCreatedException extends RuntimeException {
    public PersonNotCreatedException(String message) {
        super(message);
    }
} 