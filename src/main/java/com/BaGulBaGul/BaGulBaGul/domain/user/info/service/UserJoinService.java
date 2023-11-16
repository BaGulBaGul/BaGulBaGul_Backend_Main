package com.BaGulBaGul.BaGulBaGul.domain.user.info.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.SocialLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.UserRegisterRequest;

public interface UserJoinService {
    void registerSocialLoginUser(SocialLoginUserJoinRequest socialLoginUserJoinRequest);
    User registerUser(UserRegisterRequest userJoinRequest);
    void deleteUser(Long userId);
}
