package com.ingemark.api.rest.impl.exception;

import com.ingemark.application.exception.DuplicateProductCodeException;
import com.ingemark.application.exception.ExchangeRateUnavailableException;
import com.ingemark.application.exception.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({ MethodArgumentNotValidException.class, IllegalArgumentException.class })
    public ResponseEntity<Object> handleBadRequest(Exception exception) {
        log.error("Bad request related error occurred", exception);

        List<String> errors;

        if (exception instanceof MethodArgumentNotValidException validationEx) {
            errors = validationEx.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
        } else if (exception instanceof IllegalArgumentException illegalArgEx) {
            errors = List.of(illegalArgEx.getMessage());
        } else {
            errors = List.of("Bad request");
        }

        return buildResponse(HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedException exception) {
        return buildResponse(HttpStatus.FORBIDDEN, exception);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(ProductNotFoundException exception) {
        return buildResponse(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler(DuplicateProductCodeException.class)
    public ResponseEntity<Object> handleDataIntegrityViolations(DuplicateProductCodeException exception) {
        return buildResponse(HttpStatus.CONFLICT, exception);
    }

    @ExceptionHandler(ExchangeRateUnavailableException.class)
    public ResponseEntity<Object> handleExchangeRateError(ExchangeRateUnavailableException exception) {
        return buildResponse(HttpStatus.SERVICE_UNAVAILABLE, exception);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleInternalServerError(Exception exception) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    private ResponseEntity<Object> buildResponse(HttpStatus status, Exception exception) {
        log.error("Exception error occurred", exception);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", exception.getMessage());

        return new ResponseEntity<>(body, status);
    }

    private ResponseEntity<Object> buildResponse(HttpStatus status, List<String> messages) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", messages);

        return new ResponseEntity<>(body, status);
    }
}
