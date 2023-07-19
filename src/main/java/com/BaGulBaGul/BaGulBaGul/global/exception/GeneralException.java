package com.BaGulBaGul.BaGulBaGul.global.exception;

import com.BaGulBaGul.BaGulBaGul.global.response.ErrorCode;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

    private final ErrorCode errorCode;

    public GeneralException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    public GeneralException(ErrorCode errorCode) {
        this(errorCode, errorCode.getMessage());
    }
}