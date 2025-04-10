package com.ofeksag.book_management.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@OpenAPIDefinition(
        info = @Info(
                title = "Book Management API",
                version = "1.0",
                description = """
This API allows authenticated users to manage a book collection — including creating, retrieving, updating, and deleting books.

Authentication:
- All endpoints require a JWT token.
- Two predefined users are available for testing:
  • admin
  • user

Roles:
- ROLE_USER can retrieve books.
- ROLE_ADMIN can perform all operations.

Requests:
- Allowed fields: title, author, publishedDate, isbn.
- Do NOT include: id or unknown fields.
- Invalid inputs return 400 with clear messages.

Testing:
- Each endpoint includes request examples (valid, invalid, duplicate, etc.).
- Error responses are standardized and documented (400, 401, 403, 404, 409).
- Copy your token from /auth/login and paste it in the "Authorize" dialog (no 'Bearer' prefix needed).
"""
        )
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new io.swagger.v3.oas.models.security.SecurityScheme()
                                .name("bearerAuth")
                                .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}