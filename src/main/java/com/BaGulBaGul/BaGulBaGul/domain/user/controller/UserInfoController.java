package com.BaGulBaGul.BaGulBaGul.domain.user.controller;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.MyUserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.OtherUserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.UserModifyRequest;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;

public interface UserInfoController {
    ApiResponse<MyUserInfoResponse> getMyUserInfo(Long userId);
    ApiResponse<Object> modifyMyUserInfo(Long userId, UserModifyRequest userModifyRequest);
    ApiResponse<OtherUserInfoResponse> getOtherUserInfo(Long userId);
}
