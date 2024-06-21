package com.BaGulBaGul.BaGulBaGul.global.response;

import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ResponseCode {

    //COMMON
    public static final ResponseCode SUCCESS = new ResponseCode("C00000", HttpStatus.OK,"SUCCESS");
    public static final ResponseCode BAD_REQUEST = new ResponseCode("C10000", HttpStatus.BAD_REQUEST,"BAD REQUEST");
    public static final ResponseCode UNAUTHORIZED = new ResponseCode("C10001", HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
    public static final ResponseCode FORBIDDEN = new ResponseCode("C10002", HttpStatus.FORBIDDEN, "FORBIDDEN");
    public static final ResponseCode NOT_FOUND = new ResponseCode("C10003", HttpStatus.NOT_FOUND, "PAGE NOT FOUND");
    public static final ResponseCode INTERNAL_SERVER_ERROR = new ResponseCode("C20000", HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR");
    //EVENT
    public static final ResponseCode EVENT_NOT_FOUND = new ResponseCode("E00000", HttpStatus.OK, "이벤트가 존재하지 않습니다");
    public static final ResponseCode EVENT_CATEGORY_NOT_EXIST = new ResponseCode("E00001", HttpStatus.OK, "존재하지 않는 카테고리입니다");
    //RECRUITMENT
    public static final ResponseCode RECRUITMENT_NOT_FOUND = new ResponseCode("R00000", HttpStatus.OK, "모집글이 존재하지 않습니다");
    //POST
    public static final ResponseCode POST_NOT_FOUND = new ResponseCode("P00000", HttpStatus.OK, "게시글이 존재하지 않습니다");
    //USER
    public static final ResponseCode USER_NOT_FOUND = new ResponseCode("U00000", HttpStatus.OK, "존재하지 않는 유저입니다");
    //ALARM
    public static final ResponseCode ALARM_NOT_FOUND = new ResponseCode("AL00000", HttpStatus.OK, "알람이 존재하지 않습니다");
    //UserJoin
    public static final ResponseCode UJ_JOINTOKEN_WRONG = new ResponseCode("UJ00000", HttpStatus.OK, "소셜 로그인 회원가입 인증 토큰이 잘못되었습니다.");
    public static final ResponseCode UJ_JOINTOKEN_EXPIRED = new ResponseCode("UJ00001", HttpStatus.OK, "소셜 로그인 회원가입 인증 토큰이 만료되었습니다. 다시 인증해 주세요.");
    //Upload
    public static final ResponseCode UPLOAD_NOT_IMAGE = new ResponseCode("UPLOAD00000", HttpStatus.OK, "이미지 파일이 아닙니다");

    public static final ResponseCode[] types = {SUCCESS, BAD_REQUEST};

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    public static ResponseCode valueOf(HttpStatus httpStatus) {
        if (httpStatus == null) { throw new GeneralException(INTERNAL_SERVER_ERROR); }

        if (httpStatus.is4xxClientError())
            return ResponseCode.BAD_REQUEST;
        else if (httpStatus.is5xxServerError())
            return ResponseCode.INTERNAL_SERVER_ERROR;
        return ResponseCode.SUCCESS;
    }
}
