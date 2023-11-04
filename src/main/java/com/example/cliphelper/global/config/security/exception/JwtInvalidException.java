package com.example.cliphelper.global.config.security.exception;

import com.example.cliphelper.global.error.BusinessException;
import com.example.cliphelper.global.error.ErrorCode;

public class JwtInvalidException extends BusinessException {
    public JwtInvalidException() {
        super(ErrorCode.JWT_AUTHENTICATION_FAILED);
    }
}
