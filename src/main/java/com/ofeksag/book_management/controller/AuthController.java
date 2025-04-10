package com.ofeksag.book_management.controller;

import com.ofeksag.book_management.dto.AuthRequestDTO;
import com.ofeksag.book_management.dto.AuthResponseDTO;
import com.ofeksag.book_management.dto.ErrorResponseDTO;
import com.ofeksag.book_management.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Authentication",
        description = """
Handles user authentication and provides JWT access tokens for authorized API usage.

Once authenticated, you can access all secured endpoints based on your role
"""
)
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login and receive JWT token",
            description = """
Submit your login credentials to receive a JWT access token.

- Two predefined users are available for testing:

  • admin - can retrieve books. Select "Admin Login" to use the admin credentials.
  
  • user - can perform all operations. Select "Regular User Login" to use the regular user credentials.


**Note:** Paste the token as-is, without adding the ‘Bearer’ prefix — the system adds it automatically.
""",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Admin Login",
                                            value = """
                                                    {
                                                      "username": "admin",
                                                      "password": "pass"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Regular User Login",
                                            value = """
                                                    {
                                                      "username": "user",
                                                      "password": "pass"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Successful Login Response",
                                    value = """
                                            {
                                              "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Unauthorized Response",
                                    value = """
                                            {
                                              "error": "Unauthorized",
                                              "message": "Invalid username or password"
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO authRequestDTO) {
        AuthResponseDTO response = authService.authenticate(authRequestDTO);
        return ResponseEntity.ok(response);
    }
}
