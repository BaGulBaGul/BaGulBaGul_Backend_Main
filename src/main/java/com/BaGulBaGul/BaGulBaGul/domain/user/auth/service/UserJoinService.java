package com.BaGulBaGul.BaGulBaGul.domain.user.auth.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.auth.dto.SocialLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.auth.dto.UserRegisterRequest;

public interface UserJoinService {
    void registerSocialLoginUser(SocialLoginUserJoinRequest socialLoginUserJoinRequest);
    User registerUser(UserRegisterRequest userJoinRequest);
}
