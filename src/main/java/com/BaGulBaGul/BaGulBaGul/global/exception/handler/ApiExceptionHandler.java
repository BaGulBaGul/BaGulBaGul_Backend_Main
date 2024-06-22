package com.BaGulBaGul.BaGulBaGul.global.exception.handler;

import com.BaGulBaGul.BaGulBaGul.domain.event.exception.CategoryNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.event.exception.EventNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.PostNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.exception.RecruitmentNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.auth.exception.JoinTokenExpiredException;
import com.BaGulBaGul.BaGulBaGul.domain.user.auth.exception.JoinTokenValidationException;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.exception.UserNotFoundException;
import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import com.BaGulBaGul.BaGulBaGul.global.exception.NoPermissionException;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode;
import com.BaGulBaGul.BaGulBaGul.global.upload.exception.NotImageException;
import java.text.MessageFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    /************************
     *   일반
     ************************/
    //범용 예외
    @ExceptionHandler({GeneralException.class})
    public ResponseEntity<Object> general(GeneralException e, WebRequest request) {
        return handleExceptionInternal(e, e.getResponseCode(), request);
    }
    //처리하지 못하는 모든 예외
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> internalServerError(Exception e, WebRequest request) {
        return handleExceptionInternal(e, ResponseCode.INTERNAL_SERVER_ERROR, request);
    }
    //권한이 없음
    @ExceptionHandler(value = NoPermissionException.class)
    public ResponseEntity<Object> noPermission(Exception e, WebRequest request) {
        return handleExceptionInternal(e, ResponseCode.FORBIDDEN, request);
    }

    //검증 예외에서 메세지를 추출하도록 재정의
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status,
            WebRequest request
    ) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        Object[] arguments = fieldError.getArguments();
        String errorMessage = MessageFormat.format(fieldError.getDefaultMessage(), arguments);
        ResponseCode responseCode = ResponseCode.builder()
                .code(ResponseCode.BAD_REQUEST.getCode())
                .httpStatus(ResponseCode.BAD_REQUEST.getHttpStatus())
                .message(errorMessage)
                .build();
        return handleExceptionInternal(ex, responseCode, request);
    }
    /************************
     *   이벤트
     ************************/
    //이벤트가 존재하지 않음
    @ExceptionHandler(value = EventNotFoundException.class)
    public ResponseEntity<Object> eventNotFound(EventNotFoundException e, WebRequest webRequest) {
        return handleExceptionInternal(e, ResponseCode.EVENT_NOT_FOUND, webRequest);
    }
    //이벤트 카테고리가 존재하지 않음
    @ExceptionHandler(value = CategoryNotFoundException.class)
    public ResponseEntity<Object> categoryNotFound(CategoryNotFoundException e, WebRequest webRequest) {
        return handleExceptionInternal(e, ResponseCode.EVENT_CATEGORY_NOT_EXIST, webRequest);
    }

    /************************
     *   모집글
     ************************/
    //모집글이 존재하지 않음
    @ExceptionHandler(value = RecruitmentNotFoundException.class)
    public ResponseEntity<Object> recruitmentNotFound(RecruitmentNotFoundException e, WebRequest webRequest) {
        return handleExceptionInternal(e, ResponseCode.RECRUITMENT_NOT_FOUND, webRequest);
    }

    /************************
     *   게시글
     ************************/
    //게시글이 존재하지 않음
    @ExceptionHandler(value = PostNotFoundException.class)
    public ResponseEntity<Object> postNotFound(PostNotFoundException e, WebRequest webRequest) {
        return handleExceptionInternal(e, ResponseCode.POST_NOT_FOUND, webRequest);
    }

    /************************
     *   유저
     ************************/
    //존재하지 않는 유저
    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<Object> userNotFound(UserNotFoundException e, WebRequest webRequest) {
        return handleExceptionInternal(e, ResponseCode.USER_NOT_FOUND, webRequest);
    }

    //잘못된 조인 토큰
    @ExceptionHandler(value = JoinTokenValidationException.class)
    public ResponseEntity<Object> joinTokenWrong(JoinTokenValidationException e, WebRequest webRequest) {
        return handleExceptionInternal(e, ResponseCode.UJ_JOINTOKEN_WRONG, webRequest);
    }

    //만료된 조인 토큰
    @ExceptionHandler(value = JoinTokenExpiredException.class)
    public ResponseEntity<Object> joinTokenExpired(JoinTokenExpiredException e, WebRequest webRequest) {
        return handleExceptionInternal(e, ResponseCode.UJ_JOINTOKEN_EXPIRED, webRequest);
    }

    /************************
     *   업로드
     ************************/
    //이미지가 아닌 파일을 업로드
    @ExceptionHandler(
            NotImageException.class
    )
    public ResponseEntity<Object> notImage(RuntimeException e, WebRequest webRequest) {
        return handleExceptionInternal(e, ResponseCode.UPLOAD_NOT_IMAGE, webRequest);
    }


    /************************
     *   스프링 내부 예외처리와 연결
     ************************/
    //스프링 기본 지원 에러처리의 body에 ApiResponse를 넣어주기 위함.
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request
    ) {
        return handleExceptionInternal(ex, ResponseCode.valueOf(status), headers, status, request);
    }

    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, ResponseCode responseCode, WebRequest request
    ) {
        return handleExceptionInternal(ex, responseCode, HttpHeaders.EMPTY, responseCode.getHttpStatus(), request);
    }

    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, ResponseCode responseCode, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        return super.handleExceptionInternal(
                ex,
                ApiResponse.of(null, responseCode),
                headers,
                status,
                request
        );
    }
}
