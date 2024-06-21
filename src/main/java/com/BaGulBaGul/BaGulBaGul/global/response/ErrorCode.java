package com.BaGulBaGul.BaGulBaGul.global.response;

import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorCode {

    //COMMON
    public static final ErrorCode SUCCESS = new ErrorCode("C00000", HttpStatus.OK,"SUCCESS");
    public static final ErrorCode BAD_REQUEST = new ErrorCode("C10000", HttpStatus.BAD_REQUEST,"BAD REQUEST");
    public static final ErrorCode UNAUTHORIZED = new ErrorCode("C10001", HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
    public static final ErrorCode FORBIDDEN = new ErrorCode("C10002", HttpStatus.FORBIDDEN, "FORBIDDEN");
    public static final ErrorCode NOT_FOUND = new ErrorCode("C10003", HttpStatus.NOT_FOUND, "PAGE NOT FOUND");
    public static final ErrorCode INTERNAL_SERVER_ERROR = new ErrorCode("C20000", HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR");
    //EVENT
    public static final ErrorCode EVENT_NOT_FOUND = new ErrorCode("E00000", HttpStatus.OK, "이벤트가 존재하지 않습니다");
    public static final ErrorCode EVENT_CATEGORY_NOT_EXIST = new ErrorCode("E00001", HttpStatus.OK, "존재하지 않는 카테고리입니다");
    //RECRUITMENT
    public static final ErrorCode RECRUITMENT_NOT_FOUND = new ErrorCode("R00000", HttpStatus.OK, "모집글이 존재하지 않습니다");
    //POST
    public static final ErrorCode POST_NOT_FOUND = new ErrorCode("P00000", HttpStatus.OK, "게시글이 존재하지 않습니다");
    //USER
    public static final ErrorCode USER_NOT_FOUND = new ErrorCode("U00000", HttpStatus.OK, "존재하지 않는 유저입니다");
    //ALARM
    public static final ErrorCode ALARM_NOT_FOUND = new ErrorCode("AL00000", HttpStatus.OK, "알람이 존재하지 않습니다");
    //UserJoin
    public static final ErrorCode UJ_JOINTOKEN_WRONG = new ErrorCode("UJ00000", HttpStatus.OK, "소셜 로그인 회원가입 인증 토큰이 잘못되었습니다.");
    public static final ErrorCode UJ_JOINTOKEN_EXPIRED = new ErrorCode("UJ00001", HttpStatus.OK, "소셜 로그인 회원가입 인증 토큰이 만료되었습니다. 다시 인증해 주세요.");
    //Upload
    public static final ErrorCode UPLOAD_NOT_IMAGE = new ErrorCode("UPLOAD00000", HttpStatus.OK, "이미지 파일이 아닙니다");

    public static final ErrorCode[] types = {SUCCESS, BAD_REQUEST};

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    public static ErrorCode valueOf(HttpStatus httpStatus) {
        if (httpStatus == null) { throw new GeneralException(INTERNAL_SERVER_ERROR); }

        if (httpStatus.is4xxClientError())
            return ErrorCode.BAD_REQUEST;
        else if (httpStatus.is5xxServerError())
            return ErrorCode.INTERNAL_SERVER_ERROR;
        return ErrorCode.SUCCESS;
    }
}
