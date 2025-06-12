package com.BaGulBaGul.BaGulBaGul.domain.user.controller;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.api.response.MyUserInfoApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.api.response.OtherUserInfoApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.api.request.UserModifyApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.UserModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserInfoService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.validation.ValidationUtil;
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
    public ApiResponse<MyUserInfoApiResponse> getMyUserInfo(
            @AuthenticationPrincipal Long userId
    ) {
        return ApiResponse.of(
                MyUserInfoApiResponse.from(userInfoService.getMyUserInfo(userId))
        );
    }

    @Override
    @PatchMapping("/info/my")
    @Operation(summary = "자기 유저 정보 수정 요청",
            description = "로그인 필수. 유저 정보 수정"
    )
    public ApiResponse<Object> modifyMyUserInfo(
            @AuthenticationPrincipal Long userId,
            @RequestBody UserModifyApiRequest userModifyApiRequest
    ) {
        UserModifyRequest userModifyRequest = userModifyApiRequest.toUserModifyRequest();
        ValidationUtil.validate(userModifyRequest);
        userInfoService.modifyUserInfo(userModifyRequest, userId);
        return ApiResponse.of(null);
    }

    @Override
    @GetMapping("/info/{userId}")
    @Operation(summary = "다른 유저 정보 조회",
            description = "비로그인 유저도 접근 가능. 제한적인 유저 정보를 반환"
    )
    public ApiResponse<OtherUserInfoApiResponse> getOtherUserInfo(
            @PathVariable(name = "userId") Long userId
    ) {
        return ApiResponse.of(
                OtherUserInfoApiResponse.from(userInfoService.getOtherUserInfo(userId))
        );
    }
}
