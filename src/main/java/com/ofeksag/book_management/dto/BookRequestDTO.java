package com.ofeksag.book_management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

@Schema(
        description = """
Represents the structure of the request body for creating or updating a book.

Only the following fields are allowed:
- title (required, not blank)
- author (required, not blank)
- publishedDate (required, format: YYYY-MM-DD)
- isbn (required, must be a 13-digit number)

Do NOT include:
- id
- any unknown fields

Requests that include unrecognized or forbidden fields will result in a 400 Bad Request.
"""
)
@JsonIgnoreProperties(ignoreUnknown = false)
public class BookRequestDTO {

    @Schema(description = "Title of the book", example = "Clean Code")
    @NotNull(message = "Title is required")
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @Schema(description = "Author of the book", example = "Robert C. Martin")
    @NotNull(message = "Author is required")
    @NotBlank(message = "Author cannot be blank")
    private String author;

    @Schema(description = "Publication date of the book (format: YYYY-MM-DD)", example = "2008-08-01")
    @NotNull(message = "Published date is required")
    private LocalDate publishedDate;

    @Schema(description = "13-digit ISBN number of the book", example = "9780132350884")
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