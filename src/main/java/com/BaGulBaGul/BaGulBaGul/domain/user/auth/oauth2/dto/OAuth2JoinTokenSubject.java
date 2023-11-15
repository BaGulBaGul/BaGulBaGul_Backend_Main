package com.BaGulBaGul.BaGulBaGul.domain.user.auth.oauth2.dto;

import com.BaGulBaGul.BaGulBaGul.domain.user.auth.oauth2.constant.OAuth2Provider;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OAuth2JoinTokenSubject {
    String socialLoginId;
    OAuth2Provider oAuth2Provider;
    @JsonCreator
    public static OAuth2JoinTokenSubject deserialize(
            @JsonProperty("socialLoginId") String socialLoginId,
            @JsonProperty("oAuth2Provider") OAuth2Provider oAuth2Provider
    ) {
        return new OAuth2JoinTokenSubject(socialLoginId, oAuth2Provider);
    }
}
