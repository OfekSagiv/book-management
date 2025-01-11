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
                "978-0747532699"));

        bookRepository.save(new Book(
                "To Kill a Mockingbird",
                "Harper Lee",
                LocalDate.of(1960, 7, 11),
                "978-0061120084"));

    }
}

