package com.BaGulBaGul.BaGulBaGul.global.auth.controller;

import com.BaGulBaGul.BaGulBaGul.global.auth.dto.ParsedAccessTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.ParsedRefreshTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.ExpiredAccessTokenException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.InvalidAccessTokenException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.RefreshTokenException;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtCookieService;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtProvider;
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

    @Override
    @PostMapping("/api/auth/refresh")
    @Operation(summary = "인증 토큰 재발급 요청",
            description = "인증 토큰 재발급 요청"
    )
    public ApiResponse<Object> refresh(HttpServletRequest request, HttpServletResponse response) {
        //정보 추출
        String accessToken = jwtCookieService.getAccessToken(request);
        String refreshToken = jwtCookieService.getRefreshToken(request);
        boolean isATExpired = false;
        try {
            ParsedAccessTokenInfo parsedAccessTokenInfo = jwtProvider.parseAccessToken(accessToken);
        } catch (ExpiredAccessTokenException e) {
            isATExpired = true;
        } catch (InvalidAccessTokenException e) {
            throw new GeneralException(ResponseCode.FORBIDDEN);
        }
        //AT가 만료되지 않았다면
        if(!isATExpired) {
            throw new GeneralException(ResponseCode.FORBIDDEN);
        }
        //RT 파싱
        ParsedRefreshTokenInfo parsedRefreshTokenInfo = jwtProvider.parseRefreshToken(refreshToken);

        //검증 성공, 토큰 재발급
        Long userId = parsedRefreshTokenInfo.getUserId();
        String newAccessToken = jwtProvider.createAccessToken(userId);
        String newRefreshToken = jwtProvider.createRefreshToken(userId);

        //재발급한 토큰을 응답 쿠키에 저장
        jwtCookieService.setAccessToken(response, newAccessToken);
        jwtCookieService.setRefreshToken(response, newRefreshToken);
        return ApiResponse.of(null);
    }
}
