package com.geunnseung.daengnyangrefactor.global.exception;

import com.geunnseung.daengnyangrefactor.global.exception.dto.ErrorResponse;
import com.geunnseung.daengnyangrefactor.global.exception.dto.ErrorResponse.FieldError;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DaengnyangException.class)
    public ResponseEntity<ErrorResponse> handleDaengnyangException(
            final DaengnyangException exception,
            final HttpServletRequest request
    ) {
        ErrorCode errorCode = exception.getErrorCode();

        log.info("[DaengnyangException] code={}, message={}", errorCode.name(), exception.getMessage());

        return ResponseEntity.status(errorCode.getStatus())
                .body(ErrorResponse.from(errorCode, request.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException exception,
            final HttpServletRequest request
    ) {
        List<ErrorResponse.FieldError> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new FieldError(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()
                ))
                .toList();

        log.info("[MethodArgumentNotValidException] errors={}", errors);

        return ResponseEntity.status(ErrorCode.VALIDATION_FAILED.getStatus())
                .body(ErrorResponse.from(
                        ErrorCode.VALIDATION_FAILED,
                        request.getRequestURI(),
                        errors
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            final HttpMessageNotReadableException exception,
            final HttpServletRequest request
    ) {
        log.info("[HttpMessageNotReadableException] message={}", exception.getMessage());

        return ResponseEntity.status(ErrorCode.INVALID_REQUEST.getStatus())
                .body(ErrorResponse.from(
                        ErrorCode.INVALID_REQUEST,
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            final Exception exception,
            final HttpServletRequest request
    ) {
        log.error("[Exception] Unexpected Exception", exception);

        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ErrorResponse.from(
                        ErrorCode.INTERNAL_SERVER_ERROR,
                        request.getRequestURI()
                ));
    }
}
