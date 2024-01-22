package com.BaGulBaGul.BaGulBaGul.domain.user.auth.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.auth.exception.*;
import com.BaGulBaGul.BaGulBaGul.domain.user.auth.oauth2.dto.OAuth2JoinTokenSubject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;

import java.util.Calendar;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.Builder;

public class JwtProviderImpl implements JwtProvider {

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
    public String createAccessToken(Long userId) {
        return createToken(userId.toString(), ACCESS_TOKEN_EXPIRE_MINUTE);
    }

    @Override
    public String createRefreshToken(Long userId) {
        return createToken(userId.toString(), REFRESH_TOKEN_EXPIRE_MINUTE);
    }

    @Override
    public String createOAuth2JoinToken(OAuth2JoinTokenSubject oAuth2JoinTokenSubject) {
        ObjectMapper objectMapper = new ObjectMapper();
        String subject;
        try {
             subject = objectMapper.writeValueAsString(oAuth2JoinTokenSubject);
        } catch (JsonProcessingException e) {
            throw new JoinTokenSerializeException();
        }
        return createToken(subject, OAUTH_JOIN_TOKEN_EXPIRE_MINUTE);
    }

    @Override
    public Long getUserIdFromAccessToken(String accessToken) {
        if(accessToken == null) {
            return null;
        }
        try {
            return Long.parseLong(getSubject(accessToken));
        }
        catch (Exception ex) {
            throw new AccessTokenException();
        }
    }

    @Override
    public Long getUserIdFromRefreshToken(String refreshToken) {
        if(refreshToken == null) {
            return null;
        }
        try {
            return Long.parseLong(getSubject(refreshToken));
        }
        catch (Exception ex) {
            throw new RefreshTokenException();
        }
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
        ObjectMapper objectMapper = new ObjectMapper();
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

    private String createToken(String subject, int expireMinute) {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MINUTE, expireMinute);
        Date expiredAt = calendar.getTime();

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .setSubject(subject)
                .setIssuer(ISSUER)
                .setIssuedAt(now)
                .setExpiration(expiredAt)
                .compact();
    }
}
