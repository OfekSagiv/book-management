package com.ofeksag.book_management.validation;

import com.ofeksag.book_management.entity.Book;
import com.ofeksag.book_management.exception.BookIdNotAllowedException;

import org.springframework.stereotype.Component;

@Component
public class BookValidation {

    public void validateBookFields(Book book) {

        if (book.getId() != null)
            throw new BookIdNotAllowedException("ID must not be provided when creating or updating a book.");
    }
}