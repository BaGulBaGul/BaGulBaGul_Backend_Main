package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.AdminManageEventHostUserModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.UserModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.EventHostUserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.MyUserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.OtherUserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.UserInfoResponse;

public interface UserInfoService {
    UserInfoResponse getUserInfo(Long userId);
    MyUserInfoResponse getMyUserInfo(Long userId);
    OtherUserInfoResponse getOtherUserInfo(Long userId);
    EventHostUserInfoResponse getEventHostUserInfo(Long userId);
    void modifyUserInfo(UserModifyRequest userModifyRequest, Long userId);
    void modifyAdminManageEventHostUser(
            AdminManageEventHostUserModifyRequest adminManageEventHostUserModifyRequest, Long userId);
}
