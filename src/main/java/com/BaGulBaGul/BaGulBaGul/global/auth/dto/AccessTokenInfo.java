package com.BaGulBaGul.BaGulBaGul.global.auth.dto;

import java.util.Date;
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

    public static AccessTokenInfo from(
            JWTInfo jwtInfo,
            Long userId
    ) {
        return jwtInfo.mapBuilder(AccessTokenInfo.builder())
                .userId(userId)
                .build();
    }
}
