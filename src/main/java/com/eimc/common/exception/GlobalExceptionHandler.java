package com.eimc.common.exception;

import com.eimc.common.domain.HttpResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<HttpResponse> handleDuplicateResourceException(
            DuplicateResourceException duplicateResourceException,
            HttpServletRequest request){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                HttpResponse.builder()
                        .timeStamp(Instant.now())
                        .statusCode(HttpStatus.CONFLICT.value())
                        .status(HttpStatus.CONFLICT)
                        .errorCode("USER_DUPLICATE_EMAIL")
                        .message(duplicateResourceException.getMessage())
                        .path(request.getRequestURI())
                        .requestMethod(request.getMethod())
                        .build()

        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<HttpResponse> handleResourceNotFoundException(
            ResourceNotFoundException resourceNotFoundException,
            HttpServletRequest request){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                HttpResponse.builder()
                        .timeStamp(Instant.now())
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .status(HttpStatus.NOT_FOUND)
                        .errorCode("RESOURCE_NOT_FOUND")
                        .message(resourceNotFoundException.getMessage())
                        .path(request.getRequestURI())
                        .requestMethod(request.getMethod())
                        .build());

    }

}
