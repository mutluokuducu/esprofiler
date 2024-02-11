package com.esprofiler.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  public static final String ERROR = "error";
  public static final String ACCESS_IS_DENIED = "Access is denied";
  public static final String ERROR_OCCURRED = "An error occurred";
  public static final String MESSAGE = "message";

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<?> handleAccessDeniedException(
      AccessDeniedException ex, WebRequest request) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(Map.of(ERROR, ACCESS_IS_DENIED, MESSAGE, ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Map.of(ERROR, ERROR_OCCURRED, MESSAGE, ex.getMessage()));
  }
}
