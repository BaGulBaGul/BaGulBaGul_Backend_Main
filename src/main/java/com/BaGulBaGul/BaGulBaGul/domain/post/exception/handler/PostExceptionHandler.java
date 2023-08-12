package com.BaGulBaGul.BaGulBaGul.domain.post.exception.handler;

import com.BaGulBaGul.BaGulBaGul.domain.post.controller.PostController;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.CategoryNotFoundException;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackageClasses = PostController.class)
public class PostExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(
            CategoryNotFoundException.class
    )
    public ResponseEntity<Object> badRequest(RuntimeException e, WebRequest webRequest) {
        return callSuperHandler(e, ErrorCode.BAD_REQUEST, webRequest);
    }

    private ResponseEntity<Object> callSuperHandler(Exception e, ErrorCode errorCode, WebRequest webRequest) {
        return super.handleExceptionInternal(
                e,
                ApiResponse.of(null, errorCode),
                HttpHeaders.EMPTY,
                errorCode.getHttpStatus(),
                webRequest
        );
    }
}
