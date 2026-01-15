package com.example.githubproxy;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({UserNotFoundException.class, BranchesNotFoundException.class})
    private ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(status.value(), ex.getMessage()));
    }
}
