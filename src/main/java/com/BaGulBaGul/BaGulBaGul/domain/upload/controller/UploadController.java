package com.BaGulBaGul.BaGulBaGul.domain.upload.controller;

import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.upload.dto.UploadResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface UploadController {
    ApiResponse<UploadResponse> uploadImage(@RequestParam MultipartFile resource);
}
