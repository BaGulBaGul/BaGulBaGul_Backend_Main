package com.BaGulBaGul.BaGulBaGul.global.auth.oauth2.constant;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum OAuth2Provider {
    kakao;
    @JsonCreator
    public static OAuth2Provider deserialize(String name) {
        return OAuth2Provider.valueOf(name);
    }
}