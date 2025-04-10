package com.ofeksag.book_management.controller;

import com.ofeksag.book_management.dto.BookRequestDTO;
import com.ofeksag.book_management.dto.BookResponseDTO;
import com.ofeksag.book_management.dto.ErrorResponseDTO;
import com.ofeksag.book_management.entity.Book;
import com.ofeksag.book_management.service.BookService;
import com.ofeksag.book_management.utils.BookMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Books",
        description = """
This controller manages all book-related operations, including:

Retrieving the full list of books, Creating new book entries, Updating existing books, Deleting books by ID

Each endpoint includes predefined request examples in Swagger to help both you Provide valid input and Simulate error scenarios (e.g., invalid data, forbidden fields, duplicates)

"""
)
@RestController
@RequestMapping("/api/books")
@Validated
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(
            summary = "Retrieve all books",
            description = """
            Returns a list of all books in the system.
            
            Request must NOT include a body.
            """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request body was sent with a GET request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Body sent with GET",
                                    value = """
                                    {
                                      "error": "Bad Request",
                                      "message": "Body is not allowed for GET requests."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid token",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Unauthorized",
                                    value = """
                                    {
                                      "error": "Unauthorized",
                                      "message": "Missing or invalid authentication token"
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "User lacks sufficient permissions",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Forbidden",
                                    value = """
                                    {
                                      "error": "Access Denied",
                                      "message": "You do not have permission to access this resource"
                                    }
                                    """
                            )
                    )
            )
    })
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @Operation(
            summary = "Create a new book",
            description = """
                    Creates a new book in the system.

                    Request body must match the BookRequestDTO schema.
                   
                    Requires ROLE_ADMIN.
                    """,
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Valid Book",
                                            value = """
                                            {
                                              "title": "Clean Code",
                                              "author": "Robert C. Martin",
                                              "publishedDate": "2008-08-01",
                                              "isbn": "9780132350884"
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "Invalid - ID field present",
                                            value = """
                                            {
                                              "id": 1,
                                              "title": "Clean Architecture",
                                              "author": "Robert C. Martin",
                                              "publishedDate": "2020-05-15",
                                              "isbn": "9780134494166"
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "Invalid - Missing author",
                                            value = """
                                            {
                                              "title": "Refactoring",
                                              "publishedDate": "1999-07-08",
                                              "isbn": "9780201485677"
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "Invalid - Bad ISBN",
                                            value = """
                                            {
                                              "title": "Domain-Driven Design",
                                              "author": "Eric Evans",
                                              "publishedDate": "2003-08-30",
                                              "isbn": "123"
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "Invalid - Unknown field",
                                            value = """
                                            {
                                              "title": "Effective Java",
                                              "author": "Joshua Bloch",
                                              "publishedDate": "2018-01-06",
                                              "isbn": "9780134685991",
                                              "publisher": "Unknown"
                                            }
                                            """
                                    )
                            }
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation or structure error"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Book with the same ISBN already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Duplicate ISBN",
                                    value = """
                                    {
                                      "error": "Conflict",
                                      "message": "The book with ISBN 9780132350884 already exists in the system."
                                    }
                                    """
                            )
                    )
            )
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<BookResponseDTO> addNewBook(@Valid @RequestBody BookRequestDTO bookRequestDTO) {
        Book book = BookMapper.toEntity(bookRequestDTO);
        BookResponseDTO response = bookService.addNewBookAndReturnDTO(book);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update an existing book",
            description = """ 
                    Updates a book by ID.
                    
                    Request body must match the BookRequestDTO schema.
                   
                    Requires ROLE_ADMIN.
                    """,
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Valid Update",
                                            value = """
                                            {
                                              "title": "Clean Code Updated",
                                              "author": "Robert C. Martin",
                                              "publishedDate": "2009-01-01",
                                              "isbn": "9780132350884"
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "Invalid - ID included",
                                            value = """
                                            {
                                              "id": 77,
                                              "title": "Clean Code",
                                              "author": "Robert C. Martin",
                                              "publishedDate": "2008-08-01",
                                              "isbn": "9780132350884"
                                            }
                                            """
                                    )
                            }
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation or structure error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Book Not Found",
                                    value = """
                                    {
                                      "error": "Not Found",
                                      "message": "Book with ID 123 does not exist."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(responseCode = "409", description = "Duplicate ISBN")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequestDTO bookRequestDTO) {
        Book book = BookMapper.toEntity(bookRequestDTO);
        BookResponseDTO response = bookService.updateBookAndReturnDTO(id, book);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a book",
            description = """
            Deletes a book by its ID.
            
            No body is allowed.
            
            Requires ROLE_ADMIN.
            """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Body sent with DELETE"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Book Not Found",
                                    value = """
                                    {
                                      "error": "Not Found",
                                      "message": "Book with ID 999 was not found."
                                    }
                                    """
                            )
                    )
            )
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<BookResponseDTO> deleteBook(@PathVariable Long id) {
        BookResponseDTO response = bookService.deleteBookAndReturnDTO(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
