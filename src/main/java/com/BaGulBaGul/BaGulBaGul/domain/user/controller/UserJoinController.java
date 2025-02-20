package com.BaGulBaGul.BaGulBaGul.domain.user.controller;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.CheckDuplicateUsernameResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.SocialLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import javax.servlet.http.HttpServletResponse;

public interface UserJoinController {
    ApiResponse<Object> joinSocialLoginUser(SocialLoginUserJoinRequest socialLoginUserJoinRequest, HttpServletResponse response);
    ApiResponse<Object> deleteUser(Long userId, HttpServletResponse response);
    ApiResponse<CheckDuplicateUsernameResponse> checkDuplicateUsername(String username);
}
