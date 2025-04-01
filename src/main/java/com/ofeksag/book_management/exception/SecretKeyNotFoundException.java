package com.ofeksag.book_management.exception;

public class SecretKeyNotFoundException extends RuntimeException {
    public SecretKeyNotFoundException(String message) {
        super(message);
    }
}