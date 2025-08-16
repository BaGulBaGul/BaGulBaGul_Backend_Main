package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.SocialLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;

public interface SocialLoginUserService {
    SocialLoginUser registerSocialLoginUser(User user, String joinToken);
    void deleteSocialLoginUser(String socialLoginUserId);
}
