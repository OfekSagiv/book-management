package com.ofeksag.book_management.controller;

import com.ofeksag.book_management.dto.BookDTO;
import com.ofeksag.book_management.entity.Book;
import com.ofeksag.book_management.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<BookDTO> addNewBook(@RequestBody Book book) {
        return bookService.addNewBookAndReturnResponse(book);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.updateBookAndReturnResponse(id, book);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookDTO> deleteBook(@PathVariable Long id) {
        return bookService.deleteBookAndReturnResponse(id);
    }
}