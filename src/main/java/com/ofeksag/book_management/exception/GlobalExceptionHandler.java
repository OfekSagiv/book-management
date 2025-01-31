package com.ofeksag.book_management.exception;


import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.ofeksag.book_management.dto.BookDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookAlreadyExistsException.class)
    public ResponseEntity<BookDTO> handleBookAlreadyExistsException(BookAlreadyExistsException ex) {
        BookDTO response = new BookDTO(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<BookDTO> handleBookNotFoundException(BookNotFoundException ex) {
        BookDTO response = new BookDTO(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BookDTO> handleIllegalArgument(IllegalArgumentException ex) {
        BookDTO response = new BookDTO(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookIdNotAllowedException.class)
    public ResponseEntity<BookDTO> handleBookIdNotAllowedException(BookIdNotAllowedException ex) {
        BookDTO response = new BookDTO(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity<BookDTO> handleUnrecognizedPropertyException(UnrecognizedPropertyException ex) {
        BookDTO response = new BookDTO("Unrecognized one field or more. You should provide just: title, author, publish date, isbn.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RequestBodyNotAllowedException.class)
    public ResponseEntity<BookDTO> handleRequestBodyNotAllowedException(RequestBodyNotAllowedException ex) {
        BookDTO response = new BookDTO(ex.getMessage());
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
}