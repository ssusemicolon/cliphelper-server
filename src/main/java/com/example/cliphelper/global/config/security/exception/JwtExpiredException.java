package com.example.cliphelper.global.config.security.exception;

import com.example.cliphelper.global.error.BusinessException;
import com.example.cliphelper.global.error.ErrorCode;

public class JwtExpiredException extends BusinessException {
    public JwtExpiredException() {
        super(ErrorCode.JWT_EXPIRED);
    }
}
