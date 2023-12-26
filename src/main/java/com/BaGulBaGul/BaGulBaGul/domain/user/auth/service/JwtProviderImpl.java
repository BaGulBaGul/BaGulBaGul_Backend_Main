package com.BaGulBaGul.BaGulBaGul.domain.user.auth.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.auth.exception.JoinTokenDeserializeException;
import com.BaGulBaGul.BaGulBaGul.domain.user.auth.exception.JoinTokenExpiredException;
import com.BaGulBaGul.BaGulBaGul.domain.user.auth.exception.JoinTokenSerializeException;
import com.BaGulBaGul.BaGulBaGul.domain.user.auth.exception.JoinTokenValidationException;
import com.BaGulBaGul.BaGulBaGul.domain.user.auth.oauth2.dto.OAuth2JoinTokenSubject;
import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import com.BaGulBaGul.BaGulBaGul.global.response.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;

import java.util.Calendar;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtProviderImpl implements JwtProvider {
    @Value("${jwt.secret_key}")
    private String SECRET_KEY;

    @Value("${jwt.issuer}")
    private String ISSUER;

    @Value("${user.login.access_token_expire_minute}")
    private int ACCESS_TOKEN_EXPIRE_MINUTE;

    @Value("${user.login.refresh_token_expire_minute}")
    private int REFRESH_TOKEN_EXPIRE_MINUTE;

    @Value("${user.join.oauth_join_token_expire_minute}")
    private int OAUTH_JOIN_TOKEN_EXPIRE_MINUTE;


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
                    .setSigningKey(SECRET_KEY)
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
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setSubject(subject)
                .setIssuer(ISSUER)
                .setIssuedAt(now)
                .setExpiration(expiredAt)
                .compact();
    }
}
