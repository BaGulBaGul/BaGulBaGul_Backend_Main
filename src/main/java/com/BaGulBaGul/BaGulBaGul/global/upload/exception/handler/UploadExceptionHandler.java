package com.BaGulBaGul.BaGulBaGul.global.upload.exception.handler;

import com.BaGulBaGul.BaGulBaGul.global.exception.handler.ApiExceptionHandler;
import com.BaGulBaGul.BaGulBaGul.global.response.ErrorCode;
import com.BaGulBaGul.BaGulBaGul.global.upload.controller.UploadController;
import com.BaGulBaGul.BaGulBaGul.global.upload.exception.NotImageException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice(basePackageClasses = UploadController.class)
@Order(1)
public class UploadExceptionHandler extends ApiExceptionHandler {
    @ExceptionHandler(
            NotImageException.class
    )
    public ResponseEntity<Object> notImage(RuntimeException e, WebRequest webRequest) {
        return handleExceptionInternal(e, ErrorCode.UPLOAD_NOT_IMAGE, webRequest);
    }

    @ExceptionHandler(AmazonS3Exception.class)
    public ResponseEntity<Object> s3Error(AmazonS3Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ErrorCode.INTERNAL_SERVER_ERROR, request);
    }
}
