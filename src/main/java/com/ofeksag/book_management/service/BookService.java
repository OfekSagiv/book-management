package com.ofeksag.book_management.service;

import com.ofeksag.book_management.dto.BookDTO;
import com.ofeksag.book_management.exception.BookAlreadyExistsException;
import com.ofeksag.book_management.exception.BookNotFoundException;
import com.ofeksag.book_management.repository.BookRepository;
import com.ofeksag.book_management.validation.BookValidation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import com.ofeksag.book_management.entity.Book;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookValidation bookValidation;

    public BookService(BookRepository bookRepository, BookValidation bookValidation) {
        this.bookRepository = bookRepository;
        this.bookValidation = bookValidation;
    }

    public List<Book> getAllBooks() {

        return bookRepository.findAll();
    }

    public Book addNewBook(Book book) {
        bookValidation.validateBookFields(book);

        if (bookRepository.existsByIsbn(book.getIsbn()))
            throw new BookAlreadyExistsException("The book with ISBN " + book.getIsbn() + " already exists in the system.");

        return bookRepository.save(book);
    }

    public BookDTO addNewBookAndReturnDTO(Book book) {
        Book saved = addNewBook(book);
        return new BookDTO("Book added successfully.", saved.getId());
    }

    public void deleteBook(Long id) {


        if (!bookRepository.existsById(id))
            throw new BookNotFoundException("Book with ID " + id + " not found.");

        bookRepository.deleteById(id);
    }

    public BookDTO deleteBookAndReturnDTO(Long id) {
        deleteBook(id);
        return new BookDTO("Book with ID " + id + " deleted successfully.", id);
    }

    public Book updateBook(Long id, Book newBook) {
        bookValidation.validateBookFields(newBook);
        Optional<Book> optionalBook = bookRepository.findById(id);

        if (optionalBook.isEmpty())
            throw new BookNotFoundException("Book with ID " + id + " does not exist.");

        Book existingBook = optionalBook.get();

        if (!existingBook.getIsbn().equals(newBook.getIsbn()) && bookRepository.existsByIsbn(newBook.getIsbn()))
            throw new BookAlreadyExistsException("Another book with the same ISBN already exists.");

        existingBook.setTitle(newBook.getTitle());
        existingBook.setAuthor(newBook.getAuthor());
        existingBook.setIsbn(newBook.getIsbn());
        existingBook.setPublishedDate(newBook.getPublishedDate());

        return bookRepository.save(existingBook);
    }

    public BookDTO updateBookAndReturnDTO(Long id, Book newBook) {
        Book updated = updateBook(id, newBook);
        return new BookDTO("Book updated successfully.", updated.getId());
    }
}
