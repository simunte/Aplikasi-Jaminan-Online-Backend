package com.ebizcipta.ajo.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String MESSAGE = "Constraint violation";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(Instant.now().getEpochSecond(), ex.getMessage(),
                request.getMethod(), getUri(request), null);

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globleExcpetionHandler(Exception ex, HttpServletRequest request) {
        String uri = getUri(request);
        ErrorDetails errorDetails = new ErrorDetails(Instant.now().getEpochSecond(), ex.getMessage(),
                request.getMethod(), uri, null);

        log.error(request.getMethod() + ": " + uri, ex);

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> accessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(Instant.now().getEpochSecond(), ex.getMessage(),
                request.getMethod(), getUri(request), null);

        return  new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> constraintValidationException(ConstraintViolationException ex, HttpServletRequest request) {
        List<Violation> violations = new ArrayList<>();

        for (ConstraintViolation violation : ex.getConstraintViolations()) {
            violations.add(new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
        }

        ErrorDetails errorDetails = new ErrorDetails(Instant.now().getEpochSecond(), MESSAGE,
                request.getMethod(), getUri(request), violations);

        return  new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<Violation> violations = new ArrayList<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            violations.add(new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
        }

        ErrorDetails errorDetails = new ErrorDetails(Instant.now().getEpochSecond(), MESSAGE,
                request.getMethod(), getUri(request), violations);

        return  new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    private String getUri(HttpServletRequest request) {
        if (request.getQueryString() != null) {
            return request.getRequestURI() + "?" + request.getQueryString();
        } else {
            return request.getRequestURI();
        }
    }
}
