package com.BaGulBaGul.BaGulBaGul.domain.user.controller;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.api.response.CheckDuplicateUsernameApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.api.request.SocialLoginUserJoinApiRequest;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import javax.servlet.http.HttpServletResponse;

public interface UserJoinController {
    ApiResponse<Object> joinSocialLoginUser(SocialLoginUserJoinApiRequest socialLoginUserJoinApiRequest, HttpServletResponse response);
    ApiResponse<Object> deleteUser(Long userId, HttpServletResponse response);
    ApiResponse<CheckDuplicateUsernameApiResponse> checkDuplicateUsername(String username);
}
