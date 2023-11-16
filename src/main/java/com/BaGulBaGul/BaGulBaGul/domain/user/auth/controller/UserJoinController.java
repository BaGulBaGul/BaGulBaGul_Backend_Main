package com.BaGulBaGul.BaGulBaGul.domain.user.auth.controller;

import com.BaGulBaGul.BaGulBaGul.domain.user.auth.dto.SocialLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;

public interface UserJoinController {
    ApiResponse<Object> joinSocialLoginUser(SocialLoginUserJoinRequest socialLoginUserJoinRequest);
}
