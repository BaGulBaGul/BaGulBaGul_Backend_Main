package com.BaGulBaGul.BaGulBaGul.domain.event.exception.handler;

import com.BaGulBaGul.BaGulBaGul.domain.event.controller.EventController;
import com.BaGulBaGul.BaGulBaGul.domain.event.exception.CategoryNotFoundException;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackageClasses = EventController.class)
public class EventExceptionHandler extends ResponseEntityExceptionHandler {
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
