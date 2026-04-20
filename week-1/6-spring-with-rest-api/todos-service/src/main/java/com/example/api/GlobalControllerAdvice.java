package com.example.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.example.exception.TodoNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerAdvice {

    // Handle TodoNotFoundException globally
    @org.springframework.web.bind.annotation.ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<?> handleTodoNotFound(TodoNotFoundException ex, HttpServletRequest request) {
        HttpError httpError = new HttpError(
                request.getRequestURI(),
                request.getMethod(),
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(httpError);
    }

}
