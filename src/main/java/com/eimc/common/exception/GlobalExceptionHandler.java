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

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<HttpResponse> handlePasswordMismatchException(
            PasswordMismatchException passwordMismatchException,
            HttpServletRequest request){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                HttpResponse.builder()
                        .timeStamp(Instant.now())
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .status(HttpStatus.BAD_REQUEST)
                        .errorCode("PASSWORD_MISMATCH")
                        .message(passwordMismatchException.getMessage())
                        .path(request.getRequestURI())
                        .requestMethod(request.getMethod())
                        .build());

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> handleBadCredentialsException(
            BadCredentialsException badCredentialsException,
            HttpServletRequest request){

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                HttpResponse.builder()
                        .timeStamp(Instant.now())
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .status(HttpStatus.UNAUTHORIZED)
                        .errorCode("BAD_PASSWORD_CREDENTIALS")
                        .message(badCredentialsException.getMessage())
                        .path(request.getRequestURI())
                        .requestMethod(request.getMethod())
                        .build());

    }

}
