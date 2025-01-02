package com.ofeksag.book_management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookDTO {
    private String message;
    private Long id;

    public BookDTO(String message, Long id) {
        this.message = message;
        this.id = id;
    }

    public BookDTO(String message) {
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