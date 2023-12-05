package com.BaGulBaGul.BaGulBaGul.global.upload.controller;

import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ErrorCode;
import com.BaGulBaGul.BaGulBaGul.global.upload.dto.UploadResponse;
import com.BaGulBaGul.BaGulBaGul.global.upload.service.ImageUploadService;
import com.BaGulBaGul.BaGulBaGul.global.upload.service.ImageUploadServiceImpl;
import com.BaGulBaGul.BaGulBaGul.global.upload.service.ResourceService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class UploadControllerImpl implements UploadController {

    private final ImageUploadService imageUploadService;
    private final ResourceService resourceService;

    @Override
    @PostMapping("/api/upload/image")
    public ApiResponse<UploadResponse> uploadImage(@RequestParam MultipartFile imageFile) {
        Long resourceId;
        try {
            resourceId = imageUploadService.uploadImage(imageFile);
        } catch (IOException e) {
            throw new GeneralException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return ApiResponse.of(
                new UploadResponse(
                        resourceId,
                        resourceService.getResourceUrlFromId(resourceId)
                )
        );
    }
}
