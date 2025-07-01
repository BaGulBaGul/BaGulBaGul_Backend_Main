package com.BaGulBaGul.BaGulBaGul.global.auth.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class JWTInfo {
    String jwt;
    String jti;
    Date issuedAt;
    Date expireAt;
    String subject;

//    protected <C extends JWTInfo, T extends JWTInfoBuilder<C, T>> T mapBuilder(T builder) {
    protected <T extends JWTInfoBuilder> T mapBuilder(T builder) {
        return (T) builder
                .jwt(jwt)
                .jti(jti)
                .issuedAt(issuedAt)
                .expireAt(expireAt)
                .subject(subject);
    }
}
