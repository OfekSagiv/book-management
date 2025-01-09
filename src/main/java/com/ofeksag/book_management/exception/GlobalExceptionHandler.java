package com.ofeksag.book_management.exception;

import com.ofeksag.book_management.dto.BookDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}