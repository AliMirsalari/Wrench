package com.ali.mirsalari.wrench.exception.handler;

import com.ali.mirsalari.wrench.controller.dto.ErrorResponse;
import com.ali.mirsalari.wrench.exception.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> nullPointerExceptionHandler(NullPointerException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<String> duplicateExceptionHandler(DuplicateException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> notFoundExceptionHandler(NotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler(NotValidPasswordException.class)
    public ResponseEntity<String> notValidPasswordExceptionHandler(NotValidPasswordException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(NotValidPriceException.class)
    public ResponseEntity<String> notValidPriceExceptionHandler(NotValidPriceException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(NotValidServiceException.class)
    public ResponseEntity<String> notValidServiceExceptionHandler(NotValidServiceException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(NotValidTimeException.class)
    public ResponseEntity<String> notValidTimeExceptionHandler(NotValidTimeException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(ServiceAlreadyExistsException.class)
    public ResponseEntity<String> serviceAlreadyExistsExceptionHandler(ServiceAlreadyExistsException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(WriteImageException.class)
    public ResponseEntity<String> writeImageExceptionHandler(WriteImageException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
    }
    @ExceptionHandler(DeactivatedAccountException.class)
    public ResponseEntity<String> deactivatedAccountExceptionHandler(DeactivatedAccountException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> illegalStateExceptionHandler(IllegalStateException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        ErrorResponse errorDetails = new ErrorResponse(LocalDateTime.now(),
                "TotalErrors:" + ex.getErrorCount() + " First Error:" + Objects.requireNonNull(ex.getFieldError()).getDefaultMessage(),
                400);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
