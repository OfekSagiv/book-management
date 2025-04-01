package com.ofeksag.book_management.exception;

public class SecretKeyTooShortException extends RuntimeException {
    public SecretKeyTooShortException(String message) {
        super(message);
    }
}