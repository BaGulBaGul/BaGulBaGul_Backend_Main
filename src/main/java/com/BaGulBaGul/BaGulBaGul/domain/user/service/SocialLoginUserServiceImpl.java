package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.SocialLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.SocialLoginUserRepository;
import com.BaGulBaGul.BaGulBaGul.global.auth.oauth2.constant.OAuth2Provider;
import com.BaGulBaGul.BaGulBaGul.global.auth.oauth2.dto.OAuth2JoinTokenSubject;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialLoginUserServiceImpl implements SocialLoginUserService {

    private final SocialLoginUserRepository socialLoginUserRepository;
    private final JwtProvider jwtProvider;

    @Override
    public SocialLoginUser registerSocialLoginUser(User user, String joinToken) {
        //joinToken에서 OAuth2JoinTokenSubject 추출.
        OAuth2JoinTokenSubject oAuth2JoinTokenSubject = jwtProvider.getOAuth2JoinTokenSubject(joinToken);
        return registerSocialLoginUser(
                user,
                oAuth2JoinTokenSubject.getOAuth2Provider(),
                oAuth2JoinTokenSubject.getSocialLoginId()
        );
    }

    @Override
    public SocialLoginUser registerSocialLoginUser(User user, OAuth2Provider oAuthProvider, String oAuthId) {
        //소셜 유저 생성
        SocialLoginUser socialLoginUser = SocialLoginUser.builder()
                .id(oAuthId)
                .provider(oAuthProvider)
                .user(user)
                .build();
        socialLoginUser = socialLoginUserRepository.save(socialLoginUser);
        user.setSocialLoginUser(socialLoginUser);
        return socialLoginUser;
    }

    @Override
    public void deleteSocialLoginUser(String socialLoginUserId) {
        socialLoginUserRepository.deleteById(socialLoginUserId);
    }
}
