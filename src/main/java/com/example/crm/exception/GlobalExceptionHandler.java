package com.example.crm.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatusException(
            ResponseStatusException ex
    ) {

        ApiError error = new ApiError(
                ex.getStatusCode().value(),
                ex.getReason(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(error);
    }
}
