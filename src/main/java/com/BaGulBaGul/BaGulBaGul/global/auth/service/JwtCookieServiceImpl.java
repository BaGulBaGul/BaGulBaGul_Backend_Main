package com.BaGulBaGul.BaGulBaGul.global.auth.service;

import javax.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class JwtCookieServiceImpl implements JwtCookieService {

    @Value("${user.login.access_token_cookie_name}")
    private String ACCESS_TOKEN_COOKIE_NAME;

    @Value("${user.login.refresh_token_cookie_name}")
    private String REFRESH_TOKEN_COOKIE_NAME;

    @Value("${user.login.refresh_token_expire_minute}")
    private int REFRESH_TOKEN_EXPIRE_MINUTE;

    @Override
    public String getAccessToken(HttpServletRequest request) {
        return getToken(request, ACCESS_TOKEN_COOKIE_NAME);
    }

    @Override
    public String getRefreshToken(HttpServletRequest request) {
        return getToken(request, REFRESH_TOKEN_COOKIE_NAME);
    }

    @Override
    public void setAccessToken(HttpServletResponse response, String accessToken) {
        setToken(response, accessToken, ACCESS_TOKEN_COOKIE_NAME);
    }

    @Override
    public void setRefreshToken(HttpServletResponse response, String refreshToken) {
        setToken(response, refreshToken, REFRESH_TOKEN_COOKIE_NAME);
    }

    @Override
    public void deleteAccessToken(HttpServletResponse response) {
        Cookie cookie = new Cookie(ACCESS_TOKEN_COOKIE_NAME, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public void deleteRefreshToken(HttpServletResponse response) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private String getToken(HttpServletRequest request, String tokenName) {
        if(request.getCookies() == null) {
            return null;
        }
        for(Cookie cookie : request.getCookies()) {
            if(cookie.getName().equals(tokenName)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private void setToken(HttpServletResponse response, String token, String tokenName) {
        ResponseCookie tokenCookie = ResponseCookie.from(tokenName, token)
                .path("/")
                .maxAge(REFRESH_TOKEN_EXPIRE_MINUTE * 60)
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.addHeader("Set-Cookie", tokenCookie.toString());
    }
}
