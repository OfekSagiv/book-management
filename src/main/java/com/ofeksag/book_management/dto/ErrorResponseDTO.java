package com.ofeksag.book_management.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Error response structure returned for failed requests.")
public class ErrorResponseDTO {

    @Schema(description = "Short error code or type", example = "Unauthorized")
    private String error;

    @Schema(description = "Detailed description of the error", example = "Invalid username or password")
    private String message;

    public ErrorResponseDTO() {
    }

    public ErrorResponseDTO(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}