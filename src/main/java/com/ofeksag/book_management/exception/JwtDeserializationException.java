package com.ofeksag.book_management.exception;

public class JwtDeserializationException extends RuntimeException {
    public JwtDeserializationException(String message, Throwable cause) {
        super(message, cause);
    }
}