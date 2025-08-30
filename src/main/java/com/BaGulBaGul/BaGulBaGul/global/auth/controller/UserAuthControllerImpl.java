package com.BaGulBaGul.BaGulBaGul.global.auth.controller;

import com.BaGulBaGul.BaGulBaGul.domain.user.PasswordLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.PasswordLoginUserService;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.GeneralRoleType;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "유저 인증")
public class UserAuthControllerImpl implements UserAuthController {

    private final AuthTokenService authTokenService;
    private final PasswordLoginUserService passwordLoginUserService;

    @Override
    @PostMapping("/api/auth/login/password")
    @Operation(summary = "패스워드 로그인 요청",
            description = "패스워드 로그인 요청"
    )
    public ApiResponse<Object> passwordLogin(
            @RequestBody String loginId,
            @RequestBody String loginPw,
            HttpServletResponse response
    ) {
        PasswordLoginUser passwordLoginUser = passwordLoginUserService.findPasswordLoginUser(loginId, loginPw);
        Long userId = passwordLoginUser.getUser().getId();
        //토큰 발급
        authTokenService.issueToken(response, userId);
        return ApiResponse.of(null);
    }

    @Override
    @PostMapping("/api/auth/logout")
    @Operation(summary = "로그아웃 요청",
            description = "로그아웃 요청"
    )
    public ApiResponse<Object> logout(HttpServletRequest request, HttpServletResponse response) {
        authTokenService.deleteToken(request, response);
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
