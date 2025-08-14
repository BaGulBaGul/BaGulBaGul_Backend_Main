package com.BaGulBaGul.BaGulBaGul.global.auth.controller;

import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AdminUserLoginController {
    ApiResponse<Object> login(HttpServletResponse response, String loginId, String loginPassword);
}
