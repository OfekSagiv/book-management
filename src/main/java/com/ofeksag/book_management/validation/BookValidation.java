package com.ofeksag.book_management.validation;

import com.ofeksag.book_management.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookValidation {

    public void validateBookFields(Book book) {
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN must not be empty.");
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title must not be empty.");
        }

    }
}