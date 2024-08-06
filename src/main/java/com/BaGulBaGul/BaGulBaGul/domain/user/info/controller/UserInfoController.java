package com.BaGulBaGul.BaGulBaGul.domain.user.info.controller;

import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.MyUserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.OtherUserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.UserModifyRequest;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;

public interface UserInfoController {
    ApiResponse<MyUserInfoResponse> getMyUserInfo(Long userId);
    ApiResponse<Object> modifyMyUserInfo(Long userId, UserModifyRequest userModifyRequest);
    ApiResponse<OtherUserInfoResponse> getOtherUserInfo(Long userId);
}
