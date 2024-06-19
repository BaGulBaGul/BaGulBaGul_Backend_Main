package com.BaGulBaGul.BaGulBaGul.global.exception;

import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode.CodeType;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

    private final CodeType code;

    public GeneralException(CodeType code, String message) {
        super(message);
        this.code = code;
    }
    public GeneralException(CodeType code) {
        this(code, code.getMessage());
    }
}