package com.BaGulBaGul.BaGulBaGul.domain.user.exception;

public class InvalidSuspensionRequestException extends RuntimeException {
    public InvalidSuspensionRequestException() {
        super("잘못된 정지 요청");
    }
}
