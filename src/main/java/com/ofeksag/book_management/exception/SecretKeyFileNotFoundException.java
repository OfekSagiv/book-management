package com.ofeksag.book_management.exception;

public class SecretKeyFileNotFoundException extends RuntimeException {
    public SecretKeyFileNotFoundException(String message) {
        super(message);
    }
}