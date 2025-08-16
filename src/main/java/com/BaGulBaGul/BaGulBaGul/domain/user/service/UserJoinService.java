package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.AdminManageEventHostUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.SocialLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.AdminManageEventHostUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.SocialLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.UserRegisterRequest;

public interface UserJoinService {
    SocialLoginUser joinSocialLoginUser(SocialLoginUserJoinRequest socialLoginUserJoinRequest);
    AdminManageEventHostUser joinAdminManageEventHostUser(
            AdminManageEventHostUserJoinRequest eventHostUserRegisterRequest
    );
    User registerUser(UserRegisterRequest userRegisterRequest);
    void deleteUser(Long userId);

    void deleteAdminManageEventHostUser(Long adminManageEventHostUserId);
    boolean checkDuplicateUsername(String username);
}
