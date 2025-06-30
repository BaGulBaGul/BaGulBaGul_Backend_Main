package com.BaGulBaGul.BaGulBaGul.global.auth.controller;

import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AccessTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.RefreshTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.ExpiredAccessTokenException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.InvalidAccessTokenException;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.AuthTokenService;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtCookieService;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtProvider;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtStorageService;
import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "유저 인증")
public class UserAuthControllerImpl implements UserAuthController {

    private final JwtCookieService jwtCookieService;
    private final JwtProvider jwtProvider;
    private final JwtStorageService jwtStorageService;
    private final AuthTokenService authTokenService;

    @Override
    @GetMapping("/api/user/logout")
    @Operation(summary = "로그아웃 요청",
            description = "로그아웃 요청"
    )
    public ApiResponse<Object> logout(HttpServletResponse response) {
        authTokenService.deleteToken(response);
        return ApiResponse.of(null);
    }

    @Override
    @PostMapping("/api/auth/refresh")
    @Operation(summary = "인증 토큰 재발급 요청",
            description = "인증 토큰 재발급 요청"
    )
    public ApiResponse<Object> refresh(HttpServletRequest request, HttpServletResponse response) {
        authTokenService.refreshToken(request, response);
        return ApiResponse.of(null);
    }
}
