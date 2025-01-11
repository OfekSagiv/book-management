package com.ofeksag.book_management.utils;


import com.ofeksag.book_management.entity.Book;
import com.ofeksag.book_management.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;

    public DataInitializer(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        bookRepository.save(new Book(
                "Harry Potter and the Sorcerer's Stone",
                "J.K. Rowling",
                LocalDate.of(1997, 6, 26),
                "9780747532699"));

        bookRepository.save(new Book(
                "To Kill a Mockingbird",
                "Harper Lee",
                LocalDate.of(1960, 7, 11),
                "9780061120084"));

        bookRepository.save(new Book(
                "1984",
                "George Orwell",
                LocalDate.of(1949, 6, 8),
                "9780451524935"));

        bookRepository.save(new Book(
                "Pride and Prejudice",
                "Jane Austen",
                LocalDate.of(1813, 1, 28),
                "9781503290563"));

        bookRepository.save(new Book(
                "The Great Gatsby",
                "F. Scott Fitzgerald",
                LocalDate.of(1925, 4, 10),
                "9780743273565"));

        bookRepository.save(new Book(
                "Moby-Dick",
                "Herman Melville",
                LocalDate.of(1851, 10, 18),
                "9781503280786"));

        bookRepository.save(new Book(
                "The Catcher in the Rye",
                "J.D. Salinger",
                LocalDate.of(1951, 7, 16),
                "9780316769488"));

        bookRepository.save(new Book(
                "The Hobbit",
                "J.R.R. Tolkien",
                LocalDate.of(1937, 9, 21),
                "9780547928227"));

        bookRepository.save(new Book(
                "Brave New World",
                "Aldous Huxley",
                LocalDate.of(1932, 8, 30),
                "9780060850524"));

        bookRepository.save(new Book(
                "The Lord of the Rings: The Fellowship of the Ring",
                "J.R.R. Tolkien",
                LocalDate.of(1954, 7, 29),
                "9780618640157"));
    }
}

