package com.example.githubproxy;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserNotFoundAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                ex.getMessage()
        );

        return ResponseEntity
                .status(status)
                .body(errorResponse);
    }
}
