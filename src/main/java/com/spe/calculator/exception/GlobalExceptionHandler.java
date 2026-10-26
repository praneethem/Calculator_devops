package com.spe.calculator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<Map<String, Object>> handleArithmeticException(
            ArithmeticException exception) {

        return buildErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParameter(
            MissingServletRequestParameterException exception) {

        return buildErrorResponse(
                "Missing required parameter: " + exception.getParameterName()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidParameter(
            MethodArgumentTypeMismatchException exception) {

        return buildErrorResponse(
                "Invalid value for parameter: " + exception.getName()
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(
            ResponseStatusException exception) {

        String message = exception.getReason() != null
                ? exception.getReason()
                : "Request could not be processed";

        return buildErrorResponse(message);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(
            String message) {

        Map<String, Object> body = Map.of(
                "error", "Bad Request",
                "message", message,
                "status", HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.badRequest().body(body);
    }
}