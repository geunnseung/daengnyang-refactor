package com.geunnseung.daengnyangrefactor.global.exception;

import lombok.Getter;

@Getter
public class DaengnyangException extends RuntimeException {

    private final ErrorCode errorCode;

    public DaengnyangException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public DaengnyangException(
            final ErrorCode errorCode,
            final Throwable cause
    ) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}
