package com.BaGulBaGul.BaGulBaGul.domain.upload.controller;

import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode;
import com.BaGulBaGul.BaGulBaGul.domain.upload.dto.UploadResponse;
import com.BaGulBaGul.BaGulBaGul.domain.upload.service.ImageUploadService;
import com.BaGulBaGul.BaGulBaGul.domain.upload.service.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Api(tags = "업로드")
public class UploadControllerImpl implements UploadController {

    private final ImageUploadService imageUploadService;
    private final ResourceService resourceService;

    @Override
    @PostMapping("/api/upload/image")
    @Operation(summary = "이미지 파일을 받아 리소스를 등록하고 resource id를 응답",
            description = "참고 : 로그인 필요, 이미지 확장자가 아니라면 에러"
    )
    public ApiResponse<UploadResponse> uploadImage(@RequestParam MultipartFile imageFile) {
        Long resourceId;
        try {
            resourceId = imageUploadService.uploadImage(imageFile);
        } catch (IOException e) {
            throw new GeneralException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return ApiResponse.of(
                new UploadResponse(
                        resourceId,
                        resourceService.getResourceUrlFromId(resourceId)
                )
        );
    }
}
