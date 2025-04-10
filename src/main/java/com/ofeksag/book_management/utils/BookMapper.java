package com.ofeksag.book_management.utils;


import com.ofeksag.book_management.dto.BookRequestDTO;
import com.ofeksag.book_management.entity.Book;

public class BookMapper {
    public static Book toEntity(BookRequestDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPublishedDate(dto.getPublishedDate());
        book.setIsbn(dto.getIsbn());
        return book;
    }
}