package com.BaGulBaGul.BaGulBaGul.global.auth.controller;

import com.BaGulBaGul.BaGulBaGul.domain.user.PasswordLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.PasswordLoginUserService;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AccessTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.RefreshTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtCookieService;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtProvider;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserLoginControllerImpl implements AdminUserLoginController {

    private final PasswordLoginUserService passwordLoginUserService;
    private final JwtProvider jwtProvider;
    private final JwtCookieService jwtCookieService;

    @Override
    public ApiResponse<Object> login(HttpServletResponse response, String loginId, String loginPassword) {
        PasswordLoginUser user = passwordLoginUserService.findPasswordLoginUser(loginId, loginPassword);
        AccessTokenInfo accessToken = jwtProvider.createAccessToken(user.getUser().getId());
        RefreshTokenInfo refreshToken = jwtProvider.createRefreshToken(user.getUser().getId());
        jwtCookieService.setAccessToken(response, accessToken.getJwt());
        jwtCookieService.setRefreshToken(response, refreshToken.getJwt());
        return ApiResponse.of(null);
    }
}
