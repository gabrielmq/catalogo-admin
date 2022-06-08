package com.fullcycle.catalogo.admin.infrastructure.api.controllers;

import com.fullcycle.catalogo.admin.domain.exceptions.DomainException;
import com.fullcycle.catalogo.admin.domain.validation.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static org.springframework.http.ResponseEntity.unprocessableEntity;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DomainException.class)
    public ResponseEntity<?> handleDomainException(final DomainException ex) {
        return unprocessableEntity().body(ApiError.from(ex));
    }

    record ApiError(String message, List<Error> errors) {
        public static ApiError from(final DomainException ex) {
            return new ApiError(ex.getMessage(), ex.getErrors());
        }
    }
}
