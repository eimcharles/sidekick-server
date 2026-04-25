package com.eimc.common.exception.handler;

import com.eimc.common.domain.HttpResponse;
import com.eimc.common.exception.*;
import org.springframework.security.authentication.BadCredentialsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
                        .errorCode("RESOURCE_ALREADY_EXISTS")
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

    @ExceptionHandler(InactiveResourceException.class)
    public ResponseEntity<HttpResponse> handleInactiveResourceException(
            InactiveResourceException inactiveResourceException,
            HttpServletRequest request){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                HttpResponse.builder()
                        .timeStamp(Instant.now())
                        .statusCode(HttpStatus.CONFLICT.value())
                        .status(HttpStatus.CONFLICT)
                        .errorCode("RESOURCE_INACTIVE")
                        .message(inactiveResourceException.getMessage())
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
                        .errorCode("AUTH_PASSWORD_MISMATCH")
                        .message(passwordMismatchException.getMessage())
                        .path(request.getRequestURI())
                        .requestMethod(request.getMethod())
                        .build());

    }

    @ExceptionHandler(InvalidCurrentPasswordException.class)
    public ResponseEntity<HttpResponse> handleInvalidCurrentPasswordException(
            InvalidCurrentPasswordException invalidCurrentPasswordException,
            HttpServletRequest request){

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                HttpResponse.builder()
                        .timeStamp(Instant.now())
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .status(HttpStatus.UNAUTHORIZED)
                        .errorCode("AUTH_INVALID_PASSWORD")
                        .message(invalidCurrentPasswordException.getMessage())
                        .path(request.getRequestURI())
                        .requestMethod(request.getMethod())
                        .build());

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> handleBadCredentialsException(
            BadCredentialsException badCredentialsException,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                HttpResponse.builder()
                        .timeStamp(Instant.now())
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .status(HttpStatus.UNAUTHORIZED)
                        .errorCode("AUTH_BAD_CREDENTIALS")
                        .message("Invalid password.")
                        .path(request.getRequestURI())
                        .requestMethod(request.getMethod())
                        .build());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<HttpResponse> handleUsernameNotFoundException(
            UsernameNotFoundException usernameNotFoundException,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                HttpResponse.builder()
                        .timeStamp(Instant.now())
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .status(HttpStatus.NOT_FOUND)
                        .errorCode("AUTH_USER_NOT_FOUND")
                        .message("Invalid username.")
                        .path(request.getRequestURI())
                        .requestMethod(request.getMethod())
                        .build());
    }

}
