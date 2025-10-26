package com.suinsit.leka.config;
 
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferLimitException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;
 
import lombok.extern.slf4j.Slf4j;
 
@Slf4j
@Component
@Order(-2)
@RestControllerAdvice
public class GlobalExceptionHandler {
 
    @ExceptionHandler(DataBufferLimitException.class)
    public ResponseEntity<ErrorResponse> handleDataBufferLimitException(DataBufferLimitException ex) {
        log.error("Buffer limit exceeded", ex);
        return ResponseEntity
            .status(HttpStatus.PAYLOAD_TOO_LARGE)
            .body(new ErrorResponse(
                "PAYLOAD_TOO_LARGE",
                "The request payload size exceeds the allowed limit",
                HttpStatus.PAYLOAD_TOO_LARGE.value()
            ));
    }
 
    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<ErrorResponse> handleServerWebInputException(ServerWebInputException ex) {
        log.error("Invalid input", ex);
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(
                "BAD_REQUEST",
                "Invalid input received",
                HttpStatus.BAD_REQUEST.value()
            ));
    }
 
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
            ));
    }
}
 
record ErrorResponse(String code, String message, int status) {}
