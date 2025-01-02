package com.ofeksag.book_management.service;

import com.ofeksag.book_management.dto.BookDTO;
import com.ofeksag.book_management.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.ofeksag.book_management.entity.Book;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book addNewBook(Book book) {
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            System.out.println("The book with ISBN " + book.getIsbn() + " already exists in the system.");
            return null;
        }
        return bookRepository.save(book);
    }


    public boolean deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public ResponseEntity<BookDTO> deleteBookAndReturnResponse(Long id) {
        boolean isDeleted = deleteBook(id);
        if (isDeleted) {
            BookDTO response = new BookDTO("Book with ID " + id + " deleted successfully.", id);
            return ResponseEntity.ok(response);
        } else {
            BookDTO response = new BookDTO("Book with ID " + id + " not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    public ResponseEntity<BookDTO> addNewBookAndReturnResponse(Book book) {
        Book savedBook = addNewBook(book);

        if (savedBook == null) {
            BookDTO response = new BookDTO("Failed to add book: The book already exists in the system.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        BookDTO response = new BookDTO("Book added successfully.", savedBook.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<BookDTO> updateBookAndReturnResponse(Long id, Book book) {
        if (!bookRepository.findById(id).map(Book::getIsbn).orElse("").equals(book.getIsbn())
                && bookRepository.existsByIsbn(book.getIsbn())) {
            BookDTO response = new BookDTO("Failed to update book: Another book with the same ISBN already exists.", null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }


        Book updatedBook = updateBook(id, book);
        if (updatedBook == null) {
            BookDTO response = new BookDTO("Failed to update book: The book does not exist.", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }


        BookDTO response = new BookDTO("Book updated successfully.", updatedBook.getId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private Book updateBook(Long id, Book book) {
        Optional<Book> existingBook = bookRepository.findById(id);
        if (existingBook.isEmpty()) {
            return null;
        }

        Book bookToUpdate = existingBook.get();
        bookToUpdate.setTitle(book.getTitle());
        bookToUpdate.setAuthor(book.getAuthor());
        bookToUpdate.setIsbn(book.getIsbn());
        bookToUpdate.setPublishedDate(book.getPublishedDate());
        return bookRepository.save(bookToUpdate);
    }
}