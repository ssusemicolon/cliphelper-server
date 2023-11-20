package com.example.cliphelper.global.error.exception;

import com.example.cliphelper.global.error.BusinessException;
import com.example.cliphelper.global.error.ErrorCode;

public class FileNotModifiedException extends BusinessException {
    public FileNotModifiedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
