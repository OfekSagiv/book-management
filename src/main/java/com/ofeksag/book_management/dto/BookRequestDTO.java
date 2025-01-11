package com.ofeksag.book_management.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = false)
public class BookRequestDTO {


    @NotNull(message = "Title is required")
    @NotBlank(message = "Title cannot be blank")
    private String title;


    @NotNull(message = "Author is required")
    @NotBlank(message = "Author cannot be blank")
    private String author;


    @NotNull(message = "Published date is required")
    private LocalDate publishedDate;


    @NotNull(message = "ISBN is required")
    @NotBlank(message = "ISBN cannot be blank")
    @Pattern(regexp = "\\d{13}", message = "ISBN must be a 13-digit number")
    private String isbn;

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}