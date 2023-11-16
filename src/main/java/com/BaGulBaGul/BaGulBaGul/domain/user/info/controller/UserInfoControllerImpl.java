package com.BaGulBaGul.BaGulBaGul.domain.user.info.controller;

import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.UserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.UserModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.service.UserInfoService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Api(tags = "유저 정보")
public class UserInfoControllerImpl implements UserInfoController {

    private final UserInfoService userInfoService;

    @Override
    @GetMapping("/info")
    public ApiResponse<UserInfoResponse> getUserInfo(
            @AuthenticationPrincipal Long userId
    ) {
        return ApiResponse.of(userInfoService.getUserInfo(userId));
    }

    @Override
    @PatchMapping("/info")
    public ApiResponse<Object> modifyUserInfo(
            @AuthenticationPrincipal Long userId,
            @RequestBody UserModifyRequest userModifyRequest
    ) {
        userInfoService.modifyUserInfo(userModifyRequest, userId);
        return ApiResponse.of(null);
    }
}
