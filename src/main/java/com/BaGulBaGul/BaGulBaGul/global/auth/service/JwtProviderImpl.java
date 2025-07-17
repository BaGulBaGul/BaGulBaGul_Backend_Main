package com.BaGulBaGul.BaGulBaGul.global.auth.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserRoleService;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AccessTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AccessTokenSubject;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.JWTInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.OAuth2JoinTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.RefreshTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.RefreshTokenSubject;
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
import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtProviderImpl implements JwtProvider {

    @Value("${jwt.secret_key}")
    private String SECRET_KEY_STRING;

    private final String SECRET_KEY_ALGORITHM = "HmacSHA512";

    @Value("${jwt.issuer}")
    private String ISSUER;

    @Value("${user.login.access_token_expire_minute}")
    private int ACCESS_TOKEN_EXPIRE_MINUTE;

    @Value("${user.login.refresh_token_expire_minute}")
    private int REFRESH_TOKEN_EXPIRE_MINUTE;

    @Value("${user.join.oauth_join_token_expire_minute}")
    private int OAUTH_JOIN_TOKEN_EXPIRE_MINUTE;

    private static final ObjectMapper objectMapper = JsonConfig.getObjectMapper();

    private SecretKey secretKey;

    private final UserRoleService userRoleService;

    @PostConstruct
    private void init() {
        byte[] decodedKey = SECRET_KEY_STRING.getBytes();
        this.secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, SECRET_KEY_ALGORITHM);
    }
    
    @Override
    public AccessTokenInfo createAccessToken(Long userId) {
        AccessTokenSubject accessTokenSubject = createAccessTokenSubject(userId);
        String accessTokenSubjectString = createAccessTokenSubjectString(accessTokenSubject);
        return AccessTokenInfo.from(
                createToken(accessTokenSubjectString, ACCESS_TOKEN_EXPIRE_MINUTE),
                accessTokenSubject);
    }

    @Override
    public AccessTokenInfo createAccessToken(Long userId, Date issuedAt) {
        AccessTokenSubject accessTokenSubject = createAccessTokenSubject(userId);
        String accessTokenSubjectString = createAccessTokenSubjectString(accessTokenSubject);
        return AccessTokenInfo.from(
                createToken(accessTokenSubjectString, issuedAt, ACCESS_TOKEN_EXPIRE_MINUTE),
                accessTokenSubject);
    }

    @Override
    public AccessTokenInfo createAccessToken(Long userId, Date issuedAt, Date expireAt) {
        AccessTokenSubject accessTokenSubject = createAccessTokenSubject(userId);
        String accessTokenSubjectString = createAccessTokenSubjectString(accessTokenSubject);
        return AccessTokenInfo.from(
                createToken(accessTokenSubjectString, issuedAt, expireAt),
                accessTokenSubject);
    }

    @Override
    public RefreshTokenInfo createRefreshToken(Long userId) {
        RefreshTokenSubject refreshTokenSubject = createRefreshTokenSubject(userId);
        String refreshTokenSubjectString = createRefreshTokenSubjectString(refreshTokenSubject);
        return RefreshTokenInfo.from(
                createToken(refreshTokenSubjectString, REFRESH_TOKEN_EXPIRE_MINUTE),
                refreshTokenSubject);
    }

    @Override
    public RefreshTokenInfo createRefreshToken(Long userId, Date issuedAt) {
        RefreshTokenSubject refreshTokenSubject = createRefreshTokenSubject(userId);
        String refreshTokenSubjectString = createRefreshTokenSubjectString(refreshTokenSubject);
        return RefreshTokenInfo.from(
                createToken(refreshTokenSubjectString, issuedAt, REFRESH_TOKEN_EXPIRE_MINUTE),
                refreshTokenSubject);
    }

    @Override
    public RefreshTokenInfo createRefreshToken(Long userId, Date issuedAt, Date expireAt) {
        RefreshTokenSubject refreshTokenSubject = createRefreshTokenSubject(userId);
        String refreshTokenSubjectString = createRefreshTokenSubjectString(refreshTokenSubject);
        return RefreshTokenInfo.from(
                createToken(refreshTokenSubjectString, issuedAt, expireAt),
                refreshTokenSubject);
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
        if(accessToken == null) {
            throw new InvalidAccessTokenException();
        }
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
        return createAccessTokenInfo(accessToken, claims);
    }

    @Override
    public AccessTokenInfo parseAccessTokenIfExpired(String accessToken) throws InvalidAccessTokenException {
        if(accessToken == null) {
            throw new InvalidAccessTokenException();
        }
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
        return createAccessTokenInfo(accessToken, claims);
    }

    @Override
    public AccessTokenInfo parseAccessTokenIgnoreExpiration(String accessToken) throws InvalidAccessTokenException {
        if(accessToken == null) {
            throw new InvalidAccessTokenException();
        }
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            claims = ex.getClaims();
        } catch (JwtException ex) {
            throw new InvalidAccessTokenException();
        }
        return createAccessTokenInfo(accessToken, claims);
    }

    @Override
    public RefreshTokenInfo parseRefreshToken(String refreshToken) {
        if(refreshToken == null) {
            throw new InvalidAccessTokenException();
        }
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
        return createRefreshTokenInfo(refreshToken, claims);
    }

    private AccessTokenInfo createAccessTokenInfo(String accessToken, Claims claims) {
        AccessTokenSubject accessTokenSubject = parseAccessTokenSubjectString(claims.getSubject());
        JWTInfo jwtInfo = JWTInfo.builder()
                .jwt(accessToken)
                .jti(claims.getId())
                .subject(claims.getSubject())
                .issuedAt(claims.getIssuedAt())
                .expireAt(claims.getExpiration())
                .build();
        return AccessTokenInfo.from(
                jwtInfo,
                accessTokenSubject
        );
    }

    private RefreshTokenInfo createRefreshTokenInfo(String refreshToken, Claims claims) {
        RefreshTokenSubject refreshTokenSubject = parseRefreshTokenSubjectString(claims.getSubject());
        JWTInfo jwtInfo = JWTInfo.builder()
                .jwt(refreshToken)
                .jti(claims.getId())
                .subject(claims.getSubject())
                .issuedAt(claims.getIssuedAt())
                .expireAt(claims.getExpiration())
                .build();
        return RefreshTokenInfo.from(
                jwtInfo,
                refreshTokenSubject
        );
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

    private String createAccessTokenSubjectString(AccessTokenSubject accessTokenSubject) {
        try {
            return objectMapper.writeValueAsString(accessTokenSubject);
        } catch (JsonProcessingException e) {
            throw new GeneralException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    private AccessTokenSubject createAccessTokenSubject(Long userId) {
        List<String> roles = userRoleService.getAllRole(userId);
        return AccessTokenSubject.builder()
                .userId(userId)
                .roles(roles)
                .build();
    }

    private AccessTokenSubject parseAccessTokenSubjectString(String accessTokenSubjectString) {
        try {
            return objectMapper.readValue(accessTokenSubjectString, AccessTokenSubject.class);
        } catch (JsonProcessingException e) {
            throw new GeneralException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String createRefreshTokenSubjectString(RefreshTokenSubject refreshTokenSubject) {
        try {
            return objectMapper.writeValueAsString(refreshTokenSubject);
        } catch (JsonProcessingException e) {
            throw new GeneralException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    private RefreshTokenSubject createRefreshTokenSubject(Long userId) {
        return RefreshTokenSubject.builder()
                .userId(userId)
                .build();
    }

    private RefreshTokenSubject parseRefreshTokenSubjectString(String refreshTokenSubjectString) {
        try {
            return objectMapper.readValue(refreshTokenSubjectString, RefreshTokenSubject.class);
        } catch (JsonProcessingException e) {
            throw new GeneralException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
