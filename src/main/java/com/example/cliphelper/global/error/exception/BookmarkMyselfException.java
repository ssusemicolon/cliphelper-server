package com.example.cliphelper.global.error.exception;

import com.example.cliphelper.global.error.BusinessException;
import com.example.cliphelper.global.error.ErrorCode;

public class BookmarkMyselfException extends BusinessException {
    public BookmarkMyselfException(ErrorCode errorCode) {
        super(errorCode);
    }
}
