package com.projects.url_shortener_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUrlNotFoundException(UrlNotFoundException ex) {
        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                "The requested resource was not found.",
                ex.getMessage()
        );
        // Return a ResponseEntity with the 404 status and the custom body
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}
