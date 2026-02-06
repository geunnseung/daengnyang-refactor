package com.geunnseung.daengnyangrefactor.global.exception.dto;

import com.geunnseung.daengnyangrefactor.global.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        String code,
        String message,
        String path,
        LocalDateTime timestamp,
        List<FieldError> errors
) {
    public static ErrorResponse from(
            final ErrorCode errorCode,
            final String path
    ) {
        return new ErrorResponse(
                errorCode.name(),
                errorCode.getMessage(),
                path,
                LocalDateTime.now(),
                List.of()
        );
    }

    public static ErrorResponse from(
            final ErrorCode errorCode,
            final String path,
            final List<FieldError> errors
    ) {
        return new ErrorResponse(
                errorCode.name(),
                errorCode.getMessage(),
                path,
                LocalDateTime.now(),
                errors
        );
    }

    public record FieldError(
            String field,
            String message
    ) {
    }
}