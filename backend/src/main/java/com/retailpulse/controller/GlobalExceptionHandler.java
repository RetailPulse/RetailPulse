package com.retailpulse.controller;

import com.retailpulse.controller.exception.ApplicationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> applicationExceptionHandler(ApplicationException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(), e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

}
