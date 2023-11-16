package com.BaGulBaGul.BaGulBaGul.domain.user.info.controller;

import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.UserModifyRequest;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;

public interface UserInfoController {
    ApiResponse<Object> modifyUserInfo(Long userId, UserModifyRequest userModifyRequest);
}
