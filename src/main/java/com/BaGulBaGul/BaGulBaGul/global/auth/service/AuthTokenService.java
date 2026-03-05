package com.BaGulBaGul.BaGulBaGul.global.auth.service;

import com.BaGulBaGul.BaGulBaGul.global.auth.exception.AccountSuspendedException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.ExpiredRefreshTokenException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.InvalidAccessTokenException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.InvalidRefreshTokenException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthTokenService {
    /**
     * userId에 대한 AT와 RT를 발급하고 response의 쿠키에 저장한다.
     * db(redis)에 AT와 RT의 jti쌍을 저장한다.
     * 정지된 유저라면 예외가 발생한다.
     */
    void issueToken(HttpServletResponse response, Long userId) throws AccountSuspendedException;

    /**
     * 발급한 AT, RT를 쿠키와 db(redis)에서 지운다.
     */
    void deleteToken(HttpServletRequest request, HttpServletResponse response);

    /**
     * 발급한 AT, RT를 재발급한다.
     * AT가 만료되고 RT가 만료되지 않아야 한다. 이전의 토큰은 폐기된다.
     * 잘못되거나 만료되지 않은 AT, 잘못되거나 만료된 RT, db검증 실패 시 예외
     */
    void refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws InvalidAccessTokenException, InvalidRefreshTokenException,
            ExpiredRefreshTokenException, AccountSuspendedException;
}
