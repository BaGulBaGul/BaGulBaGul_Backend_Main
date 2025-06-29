package com.BaGulBaGul.BaGulBaGul.global.auth.service;

import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AccessTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.JWTInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.OAuth2JoinTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.RefreshTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.ExpiredAccessTokenException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.ExpiredRefreshTokenException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.InvalidAccessTokenException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.InvalidRefreshTokenException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.JoinTokenDeserializeException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.JoinTokenExpiredException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.JoinTokenSerializeException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.JoinTokenValidationException;
import com.BaGulBaGul.BaGulBaGul.global.auth.oauth2.dto.OAuth2JoinTokenSubject;
import com.BaGulBaGul.BaGulBaGul.global.config.JsonConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.Builder;

public class JwtProviderImpl implements JwtProvider {

    private static final ObjectMapper objectMapper = JsonConfig.getObjectMapper();

    private final String SECRET_KEY_STRING;
    private final String SECRET_KEY_ALGORITHM;

    private final String ISSUER;
    private final int ACCESS_TOKEN_EXPIRE_MINUTE;
    private final int REFRESH_TOKEN_EXPIRE_MINUTE;
    private final int OAUTH_JOIN_TOKEN_EXPIRE_MINUTE;

    private SecretKey secretKey;

    @Builder
    private JwtProviderImpl(
            String SECRET_KEY_STRING,
            String SECRET_KEY_ALGORITHM,
            String ISSUER,
            int ACCESS_TOKEN_EXPIRE_MINUTE,
            int REFRESH_TOKEN_EXPIRE_MINUTE,
            int OAUTH_JOIN_TOKEN_EXPIRE_MINUTE
    ) {
        this.SECRET_KEY_STRING = SECRET_KEY_STRING;
        this.SECRET_KEY_ALGORITHM = SECRET_KEY_ALGORITHM;
        this.ISSUER = ISSUER;
        this.ACCESS_TOKEN_EXPIRE_MINUTE = ACCESS_TOKEN_EXPIRE_MINUTE;
        this.REFRESH_TOKEN_EXPIRE_MINUTE = REFRESH_TOKEN_EXPIRE_MINUTE;
        this.OAUTH_JOIN_TOKEN_EXPIRE_MINUTE = OAUTH_JOIN_TOKEN_EXPIRE_MINUTE;
        byte[] decodedKey = SECRET_KEY_STRING.getBytes();
        this.secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, SECRET_KEY_ALGORITHM);
    }
    
    @Override
    public AccessTokenInfo createAccessToken(Long userId) {
        return AccessTokenInfo.from(
                createToken(userId.toString(), ACCESS_TOKEN_EXPIRE_MINUTE),
                userId);
    }

    @Override
    public AccessTokenInfo createAccessToken(Long userId, Date issuedAt) {
        return AccessTokenInfo.from(
                createToken(userId.toString(), issuedAt, ACCESS_TOKEN_EXPIRE_MINUTE),
                userId);
    }

    @Override
    public AccessTokenInfo createAccessToken(Long userId, Date issuedAt, Date expireAt) {
        return AccessTokenInfo.from(
                createToken(userId.toString(), issuedAt, expireAt),
                userId);
    }

    @Override
    public RefreshTokenInfo createRefreshToken(Long userId) {
        return RefreshTokenInfo.from(
                createToken(userId.toString(), REFRESH_TOKEN_EXPIRE_MINUTE),
                userId);
    }

    @Override
    public RefreshTokenInfo createRefreshToken(Long userId, Date issuedAt) {
        return RefreshTokenInfo.from(
                createToken(userId.toString(), issuedAt, REFRESH_TOKEN_EXPIRE_MINUTE),
                userId);
    }

    @Override
    public RefreshTokenInfo createRefreshToken(Long userId, Date issuedAt, Date expireAt) {
        return RefreshTokenInfo.from(
                createToken(userId.toString(), issuedAt, expireAt),
                userId);
    }

    @Override
    public OAuth2JoinTokenInfo createOAuth2JoinToken(OAuth2JoinTokenSubject oAuth2JoinTokenSubject) {
        String subject;
        try {
             subject = objectMapper.writeValueAsString(oAuth2JoinTokenSubject);
        } catch (JsonProcessingException e) {
            throw new JoinTokenSerializeException();
        }
        JWTInfo jwtInfo = createToken(subject, OAUTH_JOIN_TOKEN_EXPIRE_MINUTE);
        return OAuth2JoinTokenInfo.from(
                jwtInfo,
                oAuth2JoinTokenSubject
        );
    }

    @Override
    public OAuth2JoinTokenSubject getOAuth2JoinTokenSubject(String joinToken) {
        String subject;
        try {
             subject = getSubject(joinToken);
        } catch (ExpiredJwtException ex) {
            throw new JoinTokenExpiredException();
        } catch (JwtException ex) {
            throw new JoinTokenValidationException();
        }

        OAuth2JoinTokenSubject oAuth2JoinTokenSubject;
        try {
             oAuth2JoinTokenSubject = objectMapper.readValue(subject, OAuth2JoinTokenSubject.class);
        } catch (JsonProcessingException e) {
            throw new JoinTokenDeserializeException();
        }
        return oAuth2JoinTokenSubject;
    }

    @Override
    public String getSubject(String token) throws JwtException {
        Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        return claims.getSubject();
    }

    @Override
    public AccessTokenInfo parseAccessToken(String accessToken) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            throw new ExpiredAccessTokenException();
        } catch (JwtException ex) {
            throw new InvalidAccessTokenException();
        }
        Long userId = Long.parseLong(claims.getSubject());

        return AccessTokenInfo.builder()
                .jti(claims.getId())
                .issuedAt(claims.getIssuedAt())
                .expireAt(claims.getExpiration())
                .userId(userId)
                .build();
    }

    @Override
    public AccessTokenInfo parseAccessTokenIfExpired(String accessToken) throws InvalidAccessTokenException {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(accessToken)
                    .getBody();
            //만료되지 않은 AT라면 null 반환
            return null;
        } catch (ExpiredJwtException ex) {
            claims = ex.getClaims();
        } catch (JwtException ex) {
            throw new InvalidAccessTokenException();
        }
        Long userId = Long.parseLong(claims.getSubject());

        return AccessTokenInfo.builder()
                .jti(claims.getId())
                .issuedAt(claims.getIssuedAt())
                .expireAt(claims.getExpiration())
                .userId(userId)
                .build();
    }

    @Override
    public RefreshTokenInfo parseRefreshToken(String refreshToken) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(refreshToken)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            throw new ExpiredRefreshTokenException();
        } catch (JwtException ex) {
            throw new InvalidRefreshTokenException();
        }
        Long userId = Long.parseLong(claims.getSubject());

        return RefreshTokenInfo.builder()
                .jti(claims.getId())
                .issuedAt(claims.getIssuedAt())
                .expireAt(claims.getExpiration())
                .userId(userId)
                .build();
    }

    private JWTInfo createToken(String subject, int expireMinute) {
        return createToken(subject, new Date(), expireMinute);
    }

    private JWTInfo createToken(String subject, Date issuedAt, int expireMinute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(issuedAt);
        calendar.add(Calendar.MINUTE, expireMinute);
        Date expiredAt = calendar.getTime();

        return createToken(subject, issuedAt, expiredAt);
    }

    private JWTInfo createToken(String subject, Date issuedAt, Date expireAt) {
        String jti = UUID.randomUUID().toString();
        String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .setSubject(subject)
                .setIssuer(ISSUER)
                .setIssuedAt(issuedAt)
                .setExpiration(expireAt)
                .setId(jti)
                .compact();
        return JWTInfo.builder()
                .jwt(jwt)
                .jti(jti)
                .subject(subject)
                .issuedAt(issuedAt)
                .expireAt(expireAt)
                .build();
    }
}
