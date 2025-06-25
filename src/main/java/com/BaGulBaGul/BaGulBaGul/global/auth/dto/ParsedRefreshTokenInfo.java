package com.BaGulBaGul.BaGulBaGul.global.auth.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ParsedRefreshTokenInfo {
    String jti;
    Date issuedAt;
    Date expireAt;
    Long userId;
}
