package com.BaGulBaGul.BaGulBaGul.global.response;

import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //COMMON
    SUCCESS("C00000", HttpStatus.OK,"SUCCESS"),
    BAD_REQUEST("C10000", HttpStatus.BAD_REQUEST,"BAD REQUEST"),
    UNAUTHORIZED("C10001", HttpStatus.UNAUTHORIZED, "UNAUTHORIZED"),
    FORBIDDEN("C10002", HttpStatus.FORBIDDEN, "FORBIDDEN"),
    NOT_FOUND("C10003", HttpStatus.NOT_FOUND, "PAGE NOT FOUND"),
    INTERNAL_SERVER_ERROR("C20000", HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR"),

    //UserJoin
    UJ_WRONG_JOINTOKEN("UJ00000", HttpStatus.OK, "소셜 로그인 회원가입 인증 토큰이 만료되었거나 잘못되었습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    public static ErrorCode valueOf(HttpStatus httpStatus) {
        if (httpStatus == null) { throw new GeneralException(INTERNAL_SERVER_ERROR); }

        return Arrays.stream(values())
                .filter(errorCode -> errorCode.getHttpStatus() == httpStatus)
                .findFirst()
                .orElseGet(() -> {
                    if (httpStatus.is4xxClientError()) { return ErrorCode.BAD_REQUEST; }
                    else if (httpStatus.is5xxServerError()) { return ErrorCode.INTERNAL_SERVER_ERROR; }
                    else { return ErrorCode.SUCCESS; }
                });
    }
}
