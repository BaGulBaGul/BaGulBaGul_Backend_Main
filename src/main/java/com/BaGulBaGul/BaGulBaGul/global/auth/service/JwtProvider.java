package com.BaGulBaGul.BaGulBaGul.global.auth.service;

import com.BaGulBaGul.BaGulBaGul.global.auth.oauth2.dto.OAuth2JoinTokenSubject;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.AccessTokenException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.JoinTokenDeserializeException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.JoinTokenExpiredException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.JoinTokenSerializeException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.JoinTokenValidationException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.RefreshTokenException;
import io.jsonwebtoken.JwtException;

public interface JwtProvider {
    String createAccessToken(Long userId);
    String createRefreshToken(Long userId);
    String createOAuth2JoinToken(OAuth2JoinTokenSubject oAuth2JoinTokenContent) throws JoinTokenSerializeException;


    Long getUserIdFromAccessToken(String accessToken) throws AccessTokenException;
    Long getUserIdFromRefreshToken(String refreshToken) throws RefreshTokenException;
    OAuth2JoinTokenSubject getOAuth2JoinTokenSubject(String joinToken) throws JoinTokenExpiredException, JoinTokenValidationException, JoinTokenDeserializeException;
    String getSubject(String token) throws JwtException;
}
