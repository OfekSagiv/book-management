package com.ofeksag.book_management.service;


import com.ofeksag.book_management.repository.BookRepository;
import org.springframework.stereotype.Service;

import com.ofeksag.book_management.entity.Book;


import java.util.List;
@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}