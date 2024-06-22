package com.BaGulBaGul.BaGulBaGul.global.exception;

import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

    private final ResponseCode responseCode;

    public GeneralException(ResponseCode responseCode, String message) {
        super(message);
        this.responseCode = responseCode;
    }
    public GeneralException(ResponseCode responseCode) {
        this(responseCode, responseCode.getMessage());
    }
}