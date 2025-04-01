package com.ofeksag.book_management.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

@AllArgsConstructor
@Entity
@Builder
@JsonIgnoreProperties(ignoreUnknown = false)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty(required = true)
    @NotNull(message = "Title is required")
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @JsonProperty(required = true)
    @NotNull(message = "Author is required")
    @NotBlank(message = "Author cannot be blank")
    private String author;

    @JsonProperty(required = true)
    @NotNull(message = "Published date is required")
    private LocalDate publishedDate;

    @JsonProperty(required = true)
    @NotNull(message = "ISBN is required")
    @NotBlank(message = "ISBN cannot be blank")
    @Pattern(regexp = "\\d{13}", message = "ISBN must be a 13-digit number")
    private String isbn;

    public Book() {
    }

    @Builder
    public Book(String title, String author, LocalDate publishedDate, String isbn) {
        this.title = title;
        this.author = author;
        this.publishedDate = publishedDate;
        this.isbn = isbn;
    }

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

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

    public void setId(Long id) {
        this.id = id;
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
