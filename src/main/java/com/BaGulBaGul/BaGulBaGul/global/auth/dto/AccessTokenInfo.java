package com.BaGulBaGul.BaGulBaGul.global.auth.dto;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class AccessTokenInfo extends JWTInfo {
    Long userId;
    List<String> roles;

    public static AccessTokenInfo from(
            JWTInfo jwtInfo,
            AccessTokenSubject accessTokenSubject
    ) {
        return jwtInfo.mapBuilder(AccessTokenInfo.builder())
                .userId(accessTokenSubject.getUserId())
                .roles(accessTokenSubject.getRoles())
                .build();
    }
}
