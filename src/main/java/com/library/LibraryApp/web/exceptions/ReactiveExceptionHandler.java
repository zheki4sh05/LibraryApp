package com.library.LibraryApp.web.exceptions;


import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class ReactiveExceptionHandler {



    @ExceptionHandler(DataIntegrityViolationException.class)
    public Mono<ResponseEntity<String>> handleResourceNotFoundException(DataIntegrityViolationException ex, ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON).
                body(ex.getMessage()));
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public Mono<ResponseEntity<String>> handleResourceNotFoundException(EntityNotFoundException ex, ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON).
                body(ex.getMessage()));
    }

    @ExceptionHandler(ChildConstraintException.class)
    public Mono<ResponseEntity<String>> handleResourceConstraintException(ChildConstraintException ex, ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON).
                body(ex.getMessage()));
    }

    @ExceptionHandler(TransientDataAccessResourceException.class)
    public Mono<ResponseEntity<String>> handleResourceNotFoundException(TransientDataAccessResourceException ex, ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON).
                body(ex.getMessage()));
    }


    @ExceptionHandler(DuplicateKeyException.class)
    public Mono<ResponseEntity<String>> handleResourceConflictException(DuplicateKeyException ex, ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body("Конфликт полей сущности"));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ResponseEntity<String>> handleWrongRequestDataFormatException(ConstraintViolationException ex, ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<String>> handleWrongRequestDataFormatException(IllegalArgumentException ex, ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ex.getMessage()));
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(TypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid UUID format");
    }







}
