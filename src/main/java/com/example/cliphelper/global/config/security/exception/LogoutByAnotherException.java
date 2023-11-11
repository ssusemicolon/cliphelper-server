package com.example.cliphelper.global.config.security.exception;

import com.example.cliphelper.global.error.BusinessException;
import com.example.cliphelper.global.error.ErrorCode;

public class LogoutByAnotherException extends BusinessException {
    public LogoutByAnotherException() {
        super(ErrorCode.LOGOUT_BY_ANOTHER);
    }
}
