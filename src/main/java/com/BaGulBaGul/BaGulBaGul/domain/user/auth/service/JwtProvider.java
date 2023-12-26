package com.BaGulBaGul.BaGulBaGul.domain.user.auth.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.auth.exception.AccessTokenException;
import com.BaGulBaGul.BaGulBaGul.domain.user.auth.exception.RefreshTokenException;
import com.BaGulBaGul.BaGulBaGul.domain.user.auth.oauth2.dto.OAuth2JoinTokenSubject;
import io.jsonwebtoken.JwtException;

public interface JwtProvider {
    String createAccessToken(Long userId);
    String createRefreshToken(Long userId);
    String createOAuth2JoinToken(OAuth2JoinTokenSubject oAuth2JoinTokenContent);


    Long getUserIdFromAccessToken(String accessToken) throws AccessTokenException;
    Long getUserIdFromRefreshToken(String refreshToken) throws RefreshTokenException;
    OAuth2JoinTokenSubject getOAuth2JoinTokenSubject(String joinToken) throws JwtException;
    String getSubject(String token) throws JwtException;
}
