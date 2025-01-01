package com.ofeksag.book_management.utils;


import com.ofeksag.book_management.entity.Book;
import com.ofeksag.book_management.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;

    public DataInitializer(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        bookRepository.save(new Book("Spring Boot Basics", "John Doe"));
        bookRepository.save(new Book("Advanced Java", "Jane Smith"));
        System.out.println("Sample books added to the database.");
    }
}