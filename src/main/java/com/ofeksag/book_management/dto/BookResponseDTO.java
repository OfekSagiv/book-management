package com.ofeksag.book_management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Response object for book-related operations. Contains a message and optionally the affected book ID.")
public class BookResponseDTO {

    @Schema(
            description = "A message describing the result of the operation.",
            example = "Book added successfully."
    )
    private String message;

    @Schema(
            description = "ID of the affected book, if applicable (e.g., when creating, updating, or deleting a book).",
            example = "45",
            nullable = true
    )
    private Long id;

    public BookResponseDTO(String message, Long id) {
        this.message = message;
        this.id = id;
    }

    public BookResponseDTO(String message) {
        this.message = message;
        this.id = null;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}