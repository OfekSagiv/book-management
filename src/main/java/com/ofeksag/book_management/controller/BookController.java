package com.ofeksag.book_management.controller;

import com.ofeksag.book_management.dto.BookDTO;
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
    public ResponseEntity<BookDTO> addNewBook(@RequestBody Book book) {
        BookDTO response = bookService.addNewBookAndReturnDTO(book);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody Book book) {
        BookDTO response = bookService.updateBookAndReturnDTO(id, book);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookDTO> deleteBook(@PathVariable Long id) {
        BookDTO response = bookService.deleteBookAndReturnDTO(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}