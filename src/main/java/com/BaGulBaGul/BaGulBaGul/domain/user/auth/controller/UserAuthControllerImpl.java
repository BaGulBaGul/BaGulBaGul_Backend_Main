package com.BaGulBaGul.BaGulBaGul.domain.user.auth.controller;

import com.BaGulBaGul.BaGulBaGul.domain.user.auth.dto.SocialLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.auth.service.JwtCookieService;
import com.BaGulBaGul.BaGulBaGul.domain.user.auth.service.UserAuthService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "유저 인증")
public class UserAuthControllerImpl implements UserAuthController {

    private final UserAuthService userService;
    private final JwtCookieService jwtCookieService;

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

    @Override
    @GetMapping("/api/user/logout")
    @Operation(summary = "로그아웃 요청",
            description = "로그아웃 요청"
    )
    public ApiResponse<Object> logout(HttpServletResponse response) {
        jwtCookieService.deleteAccessToken(response);
        jwtCookieService.deleteRefreshToken(response);
        return ApiResponse.of(null);
    }
}
