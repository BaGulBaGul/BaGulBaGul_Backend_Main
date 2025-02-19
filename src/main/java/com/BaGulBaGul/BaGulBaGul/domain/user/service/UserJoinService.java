package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.SocialLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.SocialLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.UserRegisterRequest;

public interface UserJoinService {
    SocialLoginUser registerSocialLoginUser(SocialLoginUserJoinRequest socialLoginUserJoinRequest);
    User registerUser(UserRegisterRequest userJoinRequest);
    void deleteUser(Long userId);
    boolean checkDuplicateUsername(String username);
}
