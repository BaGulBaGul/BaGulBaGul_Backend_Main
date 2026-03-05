package com.BaGulBaGul.BaGulBaGul.global.auth.dto;

import com.BaGulBaGul.BaGulBaGul.global.auth.oauth2.dto.OAuth2JoinTokenSubject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class OAuth2JoinTokenInfo extends JWTInfo {

    private OAuth2JoinTokenSubject oAuth2JoinTokenSubject;

    public static OAuth2JoinTokenInfo from(
            JWTInfo jwtInfo,
            OAuth2JoinTokenSubject oAuth2JoinTokenSubject
    ) {
        return jwtInfo.mapBuilder(OAuth2JoinTokenInfo.builder())
                .oAuth2JoinTokenSubject(oAuth2JoinTokenSubject)
                .build();
    }
}
