package com.BaGulBaGul.BaGulBaGul.domain.user.info.controller;

import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.MyUserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.OtherUserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.UserModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.service.UserInfoService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @GetMapping("/info/my")
    @Operation(summary = "자기 유저 정보 조회",
            description = "로그인 필수. 인증 토큰을 기반으로 유저 정보를 반환."
    )
    public ApiResponse<MyUserInfoResponse> getMyUserInfo(
            @AuthenticationPrincipal Long userId
    ) {
        return ApiResponse.of(userInfoService.getMyUserInfo(userId));
    }

    @Override
    @PatchMapping("/info/my")
    @Operation(summary = "자기 유저 정보 수정 요청",
            description = "로그인 필수. 유저 정보 수정"
    )
    public ApiResponse<Object> modifyMyUserInfo(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid UserModifyRequest userModifyRequest
    ) {
        userInfoService.modifyUserInfo(userModifyRequest, userId);
        return ApiResponse.of(null);
    }

    @Override
    @GetMapping("/info/{userId}")
    @Operation(summary = "다른 유저 정보 조회",
            description = "비로그인 유저도 접근 가능. 제한적인 유저 정보를 반환"
    )
    public ApiResponse<OtherUserInfoResponse> getOtherUserInfo(
            @PathVariable(name = "userId") Long userId
    ) {
        return ApiResponse.of(userInfoService.getOtherUserInfo(userId));
    }
}
