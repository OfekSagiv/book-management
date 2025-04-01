package com.ofeksag.book_management.exception;

public class EmptySecretKeyException extends RuntimeException {
    public EmptySecretKeyException(String message) {
        super(message);
    }
}