package com.ofeksag.book_management.controller;

import com.ofeksag.book_management.dto.BookResponseDTO;
import com.ofeksag.book_management.entity.Book;
import com.ofeksag.book_management.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Validated
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<BookResponseDTO> addNewBook(@Valid @NotNull @RequestBody Book book) {
        BookResponseDTO response = bookService.addNewBookAndReturnDTO(book);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(@PathVariable Long id, @Valid @NotNull @RequestBody Book book) {
        BookResponseDTO response = bookService.updateBookAndReturnDTO(id, book);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<BookResponseDTO> deleteBook(@PathVariable Long id) {
        BookResponseDTO response = bookService.deleteBookAndReturnDTO(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}