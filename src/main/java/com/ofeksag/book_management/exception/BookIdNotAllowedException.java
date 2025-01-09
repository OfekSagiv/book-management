package com.ofeksag.book_management.exception;

public class BookIdNotAllowedException extends RuntimeException {
    public BookIdNotAllowedException(String message) {
        super(message);
    }
}