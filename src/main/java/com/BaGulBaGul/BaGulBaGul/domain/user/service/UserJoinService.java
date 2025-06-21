package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.AdminManageEventHostUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.SocialLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.AdminManageEventHostUserRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.SocialLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.UserRegisterRequest;

public interface UserJoinService {
    SocialLoginUser registerSocialLoginUser(SocialLoginUserJoinRequest socialLoginUserJoinRequest);
    AdminManageEventHostUser registerAdminManageEventHostUser(
            AdminManageEventHostUserRegisterRequest eventHostUserRegisterRequest
    );
    User registerUser(UserRegisterRequest userRegisterRequest);
    void deleteUser(Long userId);

    void deleteAdminManageEventHostUser(Long adminManageEventHostUserId);
    boolean checkDuplicateUsername(String username);
}
