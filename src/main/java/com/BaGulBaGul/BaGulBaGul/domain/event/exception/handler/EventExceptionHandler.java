package com.BaGulBaGul.BaGulBaGul.domain.event.exception.handler;

import com.BaGulBaGul.BaGulBaGul.domain.event.controller.EventController;
import com.BaGulBaGul.BaGulBaGul.domain.event.exception.CategoryNotFoundException;
import com.BaGulBaGul.BaGulBaGul.global.exception.handler.ApiExceptionHandler;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackageClasses = EventController.class)
public class EventExceptionHandler extends ApiExceptionHandler {
    @ExceptionHandler(
            CategoryNotFoundException.class
    )
    public ResponseEntity<Object> badRequest(RuntimeException e, WebRequest webRequest) {
        return handleExceptionInternal(e, ErrorCode.BAD_REQUEST, webRequest);
    }
}
