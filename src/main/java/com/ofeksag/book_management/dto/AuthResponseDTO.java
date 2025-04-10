package com.ofeksag.book_management.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response returned after successful authentication.")
public class AuthResponseDTO {

    @Schema(
            description = "JWT token used to authorize further requests",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    private String token;

    public AuthResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
