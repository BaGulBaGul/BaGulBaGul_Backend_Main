package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.AdminManageEventHostUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.AdminManagePasswordLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.SocialLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.AdminManageEventHostUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.AdminManagePasswordLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.SocialLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserRegisterRequest;

public interface UserJoinService {
    SocialLoginUser joinSocialLoginUser(SocialLoginUserJoinRequest socialLoginUserJoinRequest);
    AdminManageEventHostUser joinAdminManageEventHostUser(
            AdminManageEventHostUserJoinRequest eventHostUserRegisterRequest
    );
    AdminManagePasswordLoginUser joinAdminManagePasswordLoginUser(
            AdminManagePasswordLoginUserJoinRequest adminManagePasswordLoginUserJoinRequest
    );
    User registerUser(UserRegisterRequest userRegisterRequest);
    void deleteUser(Long userId);

    void deleteAdminManageEventHostUser(Long adminManageEventHostUserId);
    void deleteAdminManagePasswordLoginUserByUserId(Long userId);
    boolean checkDuplicateUsername(String username);
}
