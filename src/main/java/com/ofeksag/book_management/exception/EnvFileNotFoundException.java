package com.ofeksag.book_management.exception;

public class EnvFileNotFoundException extends RuntimeException {
    public EnvFileNotFoundException(String message) {
        super(message);
    }
}