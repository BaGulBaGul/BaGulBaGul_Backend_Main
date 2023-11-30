package com.BaGulBaGul.BaGulBaGul.global.exception.handler;

import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ErrorCode;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Order(2)
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({GeneralException.class})
    public ResponseEntity<Object> general(GeneralException e, WebRequest request) {
        return handleExceptionInternal(e, e.getErrorCode(), request);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> internalServerError(Exception e, WebRequest request) {
        return handleExceptionInternal(e, ErrorCode.INTERNAL_SERVER_ERROR, request);
    }

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
