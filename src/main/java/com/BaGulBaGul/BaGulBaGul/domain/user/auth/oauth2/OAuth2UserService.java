package com.BaGulBaGul.BaGulBaGul.domain.user.auth.oauth2;

import com.BaGulBaGul.BaGulBaGul.domain.user.auth.oauth2.constant.OAuth2Provider;
import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import com.BaGulBaGul.BaGulBaGul.domain.user.auth.oauth2.dto.KakaoOAuth2User;
import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();
        if(provider.equals(OAuth2Provider.kakao.name())) {
            return new KakaoOAuth2User(oAuth2User.getAttributes());
        }
        throw new GeneralException(ResponseCode.BAD_REQUEST);
    }
}
