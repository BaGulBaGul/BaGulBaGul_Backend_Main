package com.BaGulBaGul.BaGulBaGul.global.auth.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class RefreshTokenInfo extends JWTInfo {
    Long userId;

    public static RefreshTokenInfo from(
            JWTInfo jwtInfo,
            Long userId
    ) {
        return jwtInfo.mapBuilder(RefreshTokenInfo.builder())
                .userId(userId)
                .build();
    }
}
