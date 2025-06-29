package com.BaGulBaGul.BaGulBaGul.global.auth.service;

import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AccessTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.JWTInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.OAuth2JoinTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.RefreshTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.ExpiredAccessTokenException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.ExpiredRefreshTokenException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.InvalidAccessTokenException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.InvalidRefreshTokenException;
import com.BaGulBaGul.BaGulBaGul.global.auth.oauth2.dto.OAuth2JoinTokenSubject;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.JoinTokenDeserializeException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.JoinTokenExpiredException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.JoinTokenSerializeException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.JoinTokenValidationException;
import io.jsonwebtoken.JwtException;
import java.util.Date;

public interface JwtProvider {
    AccessTokenInfo createAccessToken(Long userId);
    AccessTokenInfo createAccessToken(Long userId, Date issuedAt);
    AccessTokenInfo createAccessToken(Long userId, Date issuedAt, Date expireAt);
    RefreshTokenInfo createRefreshToken(Long userId);
    RefreshTokenInfo createRefreshToken(Long userId, Date issuedAt);
    RefreshTokenInfo createRefreshToken(Long userId, Date issuedAt, Date expireAt);

    /** OAuth2JoinTokenSubject를 직렬화 해서 subject로 갖는 jwt를 생성해서 반환한다. */
    OAuth2JoinTokenInfo createOAuth2JoinToken(OAuth2JoinTokenSubject oAuth2JoinTokenContent) throws JoinTokenSerializeException;

    /** joinToken jwt를 파싱해서 OAuth2JoinTokenSubject를 반환한다 */
    OAuth2JoinTokenSubject getOAuth2JoinTokenSubject(String joinToken) throws JoinTokenExpiredException, JoinTokenValidationException, JoinTokenDeserializeException;

    /** jwt의 subbject를 반환한다 */
    String getSubject(String token) throws JwtException;

    AccessTokenInfo parseAccessToken(String accessToken) throws ExpiredAccessTokenException, InvalidAccessTokenException;

    /**
     * AT가 만료되었다면 파싱해서 정보를 반환한다. 만료되지 않았다면 null을 반환
     * @return 만료되었다면 AT정보, 만료되지 않았다면 null
     */
    AccessTokenInfo parseAccessTokenIfExpired(String accessToken) throws InvalidAccessTokenException;
    RefreshTokenInfo parseRefreshToken(String refreshToken) throws ExpiredRefreshTokenException, InvalidRefreshTokenException;
}
