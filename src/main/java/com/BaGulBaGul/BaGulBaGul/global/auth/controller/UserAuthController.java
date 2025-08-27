package com.BaGulBaGul.BaGulBaGul.global.auth.controller;


import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserAuthController {
    ApiResponse<Object> passwordLogin(String loginId, String loginPw, HttpServletResponse response);
    ApiResponse<Object> logout(HttpServletRequest request, HttpServletResponse response);
    ApiResponse<Object> refresh(HttpServletRequest request, HttpServletResponse response);
}
