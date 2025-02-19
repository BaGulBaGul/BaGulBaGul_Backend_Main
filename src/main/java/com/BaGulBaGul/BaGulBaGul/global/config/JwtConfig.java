package com.BaGulBaGul.BaGulBaGul.global.config;

import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtProvider;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtProviderImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Value("${jwt.secret_key}")
    private String SECRET_KEY_STRING;

    private String SECRET_KEY_ALGORITHM = "HmacSHA512";

    @Value("${jwt.issuer}")
    private String ISSUER;

    @Value("${user.login.access_token_expire_minute}")
    private int ACCESS_TOKEN_EXPIRE_MINUTE;

    @Value("${user.login.refresh_token_expire_minute}")
    private int REFRESH_TOKEN_EXPIRE_MINUTE;

    @Value("${user.join.oauth_join_token_expire_minute}")
    private int OAUTH_JOIN_TOKEN_EXPIRE_MINUTE;

    @Bean
    public JwtProvider jwtProvider() {
        return JwtProviderImpl.builder()
                .SECRET_KEY_STRING(SECRET_KEY_STRING)
                .SECRET_KEY_ALGORITHM(SECRET_KEY_ALGORITHM)
                .ISSUER(ISSUER)
                .ACCESS_TOKEN_EXPIRE_MINUTE(ACCESS_TOKEN_EXPIRE_MINUTE)
                .REFRESH_TOKEN_EXPIRE_MINUTE(REFRESH_TOKEN_EXPIRE_MINUTE)
                .OAUTH_JOIN_TOKEN_EXPIRE_MINUTE(OAUTH_JOIN_TOKEN_EXPIRE_MINUTE)
                .build();
    }
}
