package com.BaGulBaGul.BaGulBaGul.global.auth.service;

import com.BaGulBaGul.BaGulBaGul.global.auth.dto.ParsedAccessTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.ParsedRefreshTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.ExpiredAccessTokenException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.ExpiredRefreshTokenException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.InvalidAccessTokenException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.InvalidRefreshTokenException;
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


    OAuth2JoinTokenSubject getOAuth2JoinTokenSubject(String joinToken) throws JoinTokenExpiredException, JoinTokenValidationException, JoinTokenDeserializeException;
    String getSubject(String token) throws JwtException;
    ParsedAccessTokenInfo parseAccessToken(String accessToken) throws ExpiredAccessTokenException, InvalidAccessTokenException;
    ParsedRefreshTokenInfo parseRefreshToken(String refreshToken) throws ExpiredRefreshTokenException, InvalidRefreshTokenException;
}
