package com.ofeksag.book_management.exception;

public class RequestBodyNotAllowedException extends RuntimeException {
    public RequestBodyNotAllowedException(String message) {
        super(message);
    }
}