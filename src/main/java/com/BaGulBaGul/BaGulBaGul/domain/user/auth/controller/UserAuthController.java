package com.BaGulBaGul.BaGulBaGul.domain.user.auth.controller;


import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import javax.servlet.http.HttpServletResponse;

public interface UserAuthController {
    ApiResponse<Object> logout(HttpServletResponse response);
}
