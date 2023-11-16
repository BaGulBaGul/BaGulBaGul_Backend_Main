package com.BaGulBaGul.BaGulBaGul.domain.user.info.controller;

import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.SocialLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Api(tags = "유저 회원가입")
public class UserJoinControllerImpl implements UserJoinController {

    private final UserJoinService userService;

    @Override
    @PostMapping("/api/user/join/social")
    @Operation(summary = "소셜 로그인 유저 회원가입 요청",
            description = "redirect시에 프론트에 넘긴 joinToken은 필수\n"
                    + "닉네임도 필수. 이메일은 선택사항"
    )
    public ApiResponse<Object> joinSocialLoginUser(@RequestBody @Valid SocialLoginUserJoinRequest socialLoginUserJoinRequest) {
        userService.registerSocialLoginUser(socialLoginUserJoinRequest);
        return ApiResponse.of(null);
    }
}
