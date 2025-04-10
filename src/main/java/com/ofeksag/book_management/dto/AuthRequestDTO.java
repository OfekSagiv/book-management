package com.ofeksag.book_management.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = """
Login request credentials.  
Use one of the predefined users:
- Username: admin / Password: pass
- Username: user / Password: pass
""")
public class AuthRequestDTO {

    @Schema(description = "Username of the user", example = "admin")
    private String username;

    @Schema(description = "Password of the user", example = "pass")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}