package com.example.cliphelper.global.error.exception;

import com.example.cliphelper.global.error.BusinessException;
import com.example.cliphelper.global.error.ErrorCode;

public class ArticleNotInCollectionException extends BusinessException {
    public ArticleNotInCollectionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
