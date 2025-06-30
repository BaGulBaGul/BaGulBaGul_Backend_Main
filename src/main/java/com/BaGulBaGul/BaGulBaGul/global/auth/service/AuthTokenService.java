package com.BaGulBaGul.BaGulBaGul.global.auth.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthTokenService {
    void issueToken(HttpServletResponse response, Long userId);
    void deleteToken(HttpServletResponse response);
    void refreshToken(HttpServletRequest request, HttpServletResponse response);
}
