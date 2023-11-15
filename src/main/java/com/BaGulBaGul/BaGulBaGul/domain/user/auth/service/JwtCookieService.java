package com.BaGulBaGul.BaGulBaGul.domain.user.auth.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface JwtCookieService {
    String getAccessToken(HttpServletRequest request);
    String getRefreshToken(HttpServletRequest request);
    void setAccessToken(HttpServletResponse response, String accessToken);
    void setRefreshToken(HttpServletResponse response, String refreshToken);
    void deleteAccessToken(HttpServletResponse response);
    void deleteRefreshToken(HttpServletResponse response);
}
