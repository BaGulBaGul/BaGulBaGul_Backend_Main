package com.BaGulBaGul.BaGulBaGul.domain.user.auth.controller;

import com.BaGulBaGul.BaGulBaGul.domain.user.auth.service.JwtCookieService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "유저 인증")
public class UserAuthControllerImpl implements UserAuthController {

    private final JwtCookieService jwtCookieService;

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
