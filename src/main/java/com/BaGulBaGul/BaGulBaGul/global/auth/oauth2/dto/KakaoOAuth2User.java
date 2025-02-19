package com.BaGulBaGul.BaGulBaGul.global.auth.oauth2.dto;

import com.BaGulBaGul.BaGulBaGul.global.auth.oauth2.constant.OAuth2Provider;
import java.util.Map;

public class KakaoOAuth2User extends ApplicationOAuth2User {

    public KakaoOAuth2User(Map<String, Object> attributes) {
        super(
                attributes.get("id").toString(),
                OAuth2Provider.kakao,
                attributes
        );
    }
}
