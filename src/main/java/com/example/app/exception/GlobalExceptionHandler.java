package com.example.app.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(StockConflictException.class)
  public ResponseEntity<Map<String, String>> handleStockConflict(StockConflictException ex) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT) // 409
        .body(Map.of("message", ex.getMessage()));
  }
}
