package com.BaGulBaGul.BaGulBaGul.global.exception.handler;

import com.BaGulBaGul.BaGulBaGul.domain.event.exception.CategoryNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.event.exception.EventNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.PostNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.exception.RecruitmentNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.exception.UserNotFoundException;
import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ErrorCode;
import com.BaGulBaGul.BaGulBaGul.global.upload.exception.NotImageException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return handleExceptionInternal(e, e.getErrorCode(), request);
    }
    //처리하지 못하는 모든 예외
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> internalServerError(Exception e, WebRequest request) {
        return handleExceptionInternal(e, ErrorCode.INTERNAL_SERVER_ERROR, request);
    }

    /************************
     *   이벤트
     ************************/
    //이벤트가 존재하지 않음
    @ExceptionHandler(value = EventNotFoundException.class)
    public ResponseEntity<Object> eventNotFound(EventNotFoundException e, WebRequest webRequest) {
        return handleExceptionInternal(e, ErrorCode.EVENT_NOT_FOUND, webRequest);
    }
    //이벤트 카테고리가 존재하지 않음
    @ExceptionHandler(value = CategoryNotFoundException.class)
    public ResponseEntity<Object> categoryNotFound(CategoryNotFoundException e, WebRequest webRequest) {
        return handleExceptionInternal(e, ErrorCode.EVENT_CATEGORY_NOT_EXIST, webRequest);
    }

    /************************
     *   모집글
     ************************/
    //모집글이 존재하지 않음
    @ExceptionHandler(value = RecruitmentNotFoundException.class)
    public ResponseEntity<Object> recruitmentNotFound(RecruitmentNotFoundException e, WebRequest webRequest) {
        return handleExceptionInternal(e, ErrorCode.RECRUITMENT_NOT_FOUND, webRequest);
    }

    /************************
     *   게시글
     ************************/
    //게시글이 존재하지 않음
    @ExceptionHandler(value = PostNotFoundException.class)
    public ResponseEntity<Object> postNotFound(PostNotFoundException e, WebRequest webRequest) {
        return handleExceptionInternal(e, ErrorCode.POST_NOT_FOUND, webRequest);
    }

    /************************
     *   유저
     ************************/
    //존재하지 않는 유저
    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<Object> userNotFound(UserNotFoundException e, WebRequest webRequest) {
        return handleExceptionInternal(e, ErrorCode.USER_NOT_FOUND, webRequest);
    }

    /************************
     *   업로드
     ************************/
    //이미지가 아닌 파일을 업로드
    @ExceptionHandler(
            NotImageException.class
    )
    public ResponseEntity<Object> notImage(RuntimeException e, WebRequest webRequest) {
        return handleExceptionInternal(e, ErrorCode.UPLOAD_NOT_IMAGE, webRequest);
    }


    /************************
     *   스프링 내부 예외처리와 연결
     ************************/
    //스프링 기본 지원 에러처리의 body에 ApiResponse를 넣어주기 위함.
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request
    ) {
        return handleExceptionInternal(ex, ErrorCode.valueOf(status), headers, status, request);
    }

    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, ErrorCode errorCode, WebRequest request
    ) {
        return handleExceptionInternal(ex, errorCode, HttpHeaders.EMPTY, errorCode.getHttpStatus(), request);
    }

    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, ErrorCode errorCode, HttpHeaders headers,
                                                           HttpStatus status, WebRequest request) {
        return super.handleExceptionInternal(
                ex,
                ApiResponse.of(null, errorCode),
                headers,
                status,
                request
        );
    }
}
