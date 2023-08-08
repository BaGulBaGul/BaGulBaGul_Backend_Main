package com.BaGulBaGul.BaGulBaGul.global.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.Instant;

public class JwtUtil {

    private static final Algorithm ALGORITHM = Algorithm.HMAC256("token-secret-key");
    private static final long ACCESS_TIME = 60 * 30; // 액세스 토큰 30분
    private static final long REFRESH_TIME = 60 * 60 * 24 * 14;  // 리프레시 토큰 2주

    // 액세스 토큰 생성
    public static String makeAccessToken(String name){
        return JWT.create()
                .withClaim("exp", Instant.now().getEpochSecond()+ACCESS_TIME)
                .withSubject(name)
                .sign(ALGORITHM);
    }

    // 리프레시 토큰 생성
    public static String makeRefreshToken(String name){
        return JWT.create()
                .withClaim("exp", Instant.now().getEpochSecond()+REFRESH_TIME)
                .withSubject(name)
                .sign(ALGORITHM);
    }

    public static JwtVerifyResult verify(String token){
        try{
            DecodedJWT verify = JWT.require(ALGORITHM).build().verify(token);
            return JwtVerifyResult
                    .builder()
                    .success(true)
                    .id(verify.getSubject())
                    .build();
        } catch (Exception e){
            DecodedJWT decode = JWT.decode(token);
            return JwtVerifyResult
                    .builder()
                    .success(false)
                    .id(decode.getSubject())
                    .build();
        }
    }
}
