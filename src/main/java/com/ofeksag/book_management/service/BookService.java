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

    public Book addNewBook(Book book) {
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            System.out.println("The book with ISBN " + book.getIsbn() + " already exists in the system.");
            return null;
        }
        return bookRepository.save(book);
    }


    public Book updateBook(Long id, Book book) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id " + id));

        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPublishedDate(book.getPublishedDate());
        existingBook.setIsbn(book.getIsbn());

        return bookRepository.save(existingBook);
    }

    public boolean deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}