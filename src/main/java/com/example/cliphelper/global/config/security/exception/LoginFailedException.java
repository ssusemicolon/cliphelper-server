package com.example.cliphelper.global.config.security.exception;

import com.example.cliphelper.global.error.BusinessException;
import com.example.cliphelper.global.error.ErrorCode;

public class LoginFailedException extends BusinessException {
    public LoginFailedException() {
        super(ErrorCode.AUTHENTICATION_FAILED);
    }
}
