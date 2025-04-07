package com.ofeksag.book_management.exception;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.ofeksag.book_management.dto.AuthResponseDTO;
import com.ofeksag.book_management.dto.BookResponseDTO;
import com.ofeksag.book_management.dto.ErrorResponseDTO;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookAlreadyExistsException.class)
    public ResponseEntity<BookResponseDTO> handleBookAlreadyExistsException(BookAlreadyExistsException ex) {
        BookResponseDTO response = new BookResponseDTO(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<BookResponseDTO> handleBookNotFoundException(BookNotFoundException ex) {
        BookResponseDTO response = new BookResponseDTO(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BookResponseDTO> handleIllegalArgument(IllegalArgumentException ex) {
        BookResponseDTO response = new BookResponseDTO(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookIdNotAllowedException.class)
    public ResponseEntity<BookResponseDTO> handleBookIdNotAllowedException(BookIdNotAllowedException ex) {
        BookResponseDTO response = new BookResponseDTO(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity<BookResponseDTO> handleUnrecognizedPropertyException(UnrecognizedPropertyException ex) {
        BookResponseDTO response = new BookResponseDTO("Unrecognized one field or more." +
                " You should provide just: title, author, publish date, isbn.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RequestBodyNotAllowedException.class)
    public ResponseEntity<BookResponseDTO> handleRequestBodyNotAllowedException(RequestBodyNotAllowedException ex) {
        BookResponseDTO response = new BookResponseDTO(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleFieldValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();

            if (error.getRejectedValue() instanceof String &&
                    ((String) error.getRejectedValue()).trim().isEmpty()) {
                errorMessage = fieldName + " cannot be blank";
            }

            if (error.getRejectedValue() == null) {
                errorMessage = fieldName + " is required";
            }
            errors.put(fieldName, errorMessage);
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentials(BadCredentialsException ex) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO("Invalid credentials",
                "Incorrect username or password");
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDenied(AccessDeniedException ex) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO("Access Denied",
                "You do not have permission to access this resource");
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponseDTO> handleJwtException(JwtException ex) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO("JWT Error", ex.getMessage());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(Exception ex) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO("Internal Server Error", ex.getMessage());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JwtDeserializationException.class)
    public ResponseEntity<ErrorResponseDTO> handleJwtDeserializationException(JwtDeserializationException ex) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO("JWT Deserialization Error", ex.getMessage());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MissingTokenException.class)
    public ResponseEntity<ErrorResponseDTO> handleMissingTokenException(MissingTokenException ex) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO("MissingToken", ex.getMessage());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BookResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof UnrecognizedPropertyException) {
            BookResponseDTO response = new BookResponseDTO("Unrecognized one field or more. You should provide just: title, author, publish date, isbn.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        BookResponseDTO response = new BookResponseDTO("Internal Server Error: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}