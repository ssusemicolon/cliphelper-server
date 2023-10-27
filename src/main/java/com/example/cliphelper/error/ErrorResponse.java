package com.example.cliphelper.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String code;
    private String message;

    public ErrorResponse(final ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
    public ErrorResponse(final ErrorCode errorCode, String message) {
        this.status = errorCode.getStatus();
        this.code = errorCode.getCode();
        this.message = message;
    }

    public static ErrorResponse of(final ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }

    public static ErrorResponse of(final ErrorCode errorCode, final String message) {
        return new ErrorResponse(errorCode, message);
    }
    public static ErrorResponse of(final int status, final String code, final String message) {
        return new ErrorResponse(status, code, message);
    }
}
