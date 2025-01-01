package com.ofeksag.book_management.controller;

import com.ofeksag.book_management.entity.Book;
import com.ofeksag.book_management.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping
    public ResponseEntity<Book> addNewBook(@RequestBody Book book) {
        Book savedBook = bookService.addNewBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }
}