package com.BaGulBaGul.BaGulBaGul.domain.user.auth.oauth2.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Map;

public enum OAuth2Provider {
    kakao;
    @JsonCreator
    public static OAuth2Provider deserialize(String name) {
        return OAuth2Provider.valueOf(name);
    }
}