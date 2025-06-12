package com.iliamalafeev.bookstore.bookstore_backend.utils.exceptions;
public class PersonNotFoundException extends RuntimeException {
    public PersonNotFoundException(String message) {
        super(message);
    }
}
 