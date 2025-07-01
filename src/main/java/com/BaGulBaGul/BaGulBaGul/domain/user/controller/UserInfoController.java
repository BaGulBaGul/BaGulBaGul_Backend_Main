package com.BaGulBaGul.BaGulBaGul.domain.user.controller;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.api.response.MyUserInfoApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.api.response.OtherUserInfoApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.api.request.UserModifyApiRequest;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;

public interface UserInfoController {
    ApiResponse<MyUserInfoApiResponse> getMyUserInfo(Long userId);
    ApiResponse<Object> modifyMyUserInfo(Long userId, UserModifyApiRequest userModifyApiRequest);
    ApiResponse<OtherUserInfoApiResponse> getOtherUserInfo(Long userId);
}
