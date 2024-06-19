package com.BaGulBaGul.BaGulBaGul.global.exception;

import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode;
import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode.CodeType;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

    private final ResponseCode code;

    public GeneralException(ResponseCode code, String message) {
        super(message);
        this.code = code;
    }
    public GeneralException(ResponseCode code) {
        this(code, code.getMessage());
    }
}