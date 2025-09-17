package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.SocialLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.global.auth.oauth2.constant.OAuth2Provider;

public interface SocialLoginUserService {
    SocialLoginUser registerSocialLoginUser(User user, String joinToken);
    SocialLoginUser registerSocialLoginUser(User user, OAuth2Provider oAuthProvider, String oAuthId);
    void deleteSocialLoginUser(String socialLoginUserId);
}
