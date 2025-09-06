package com.BaGulBaGul.BaGulBaGul.global.auth.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.UserSuspensionStatusResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserSuspensionService;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AccessTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.RefreshTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.AccountSuspendedException;
import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthTokenServiceImpl implements AuthTokenService {

    private final JwtProvider jwtProvider;
    private final JwtCookieService jwtCookieService;
    private final JwtStorageService jwtStorageService;
    private final UserSuspensionService userSuspensionService;

    @Override
    public void issueToken(HttpServletResponse response, Long userId) {
        //정지된 유저라면 예외
        UserSuspensionStatusResponse userSuspensionStatus = userSuspensionService.getUserSuspensionStatus(userId);
        if (userSuspensionStatus.isSuspended()) {
            throw new AccountSuspendedException();
        }
        //토큰 발급
        AccessTokenInfo atInfo = jwtProvider.createAccessToken(userId);
        RefreshTokenInfo rtInfo = jwtProvider.createRefreshToken(userId);
        //db에 저장
        jwtStorageService.save(atInfo, rtInfo);
        //쿠키 저장
        jwtCookieService.setAccessToken(response, atInfo.getJwt());
        jwtCookieService.setRefreshToken(response, rtInfo.getJwt());
    }

    @Override
    public void deleteToken(HttpServletRequest request, HttpServletResponse response) {
        //AT 추출
        String accessToken = jwtCookieService.getAccessToken(request);
        AccessTokenInfo parsedAccessTokenInfo = jwtProvider.parseAccessTokenIgnoreExpiration(accessToken);

        //db에서 삭제
        jwtStorageService.delete(parsedAccessTokenInfo);

        //쿠키 삭제
        jwtCookieService.deleteAccessToken(response);
        jwtCookieService.deleteRefreshToken(response);
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        //정보 추출
        String accessToken = jwtCookieService.getAccessToken(request);
        String refreshToken = jwtCookieService.getRefreshToken(request);
        //AT 파싱. 잘못된 토큰은 예외
        AccessTokenInfo parsedAccessTokenInfo = jwtProvider.parseAccessTokenIfExpired(accessToken);
        //AT가 만료되지 않았는데 재발급 시도 => 잘못된 접근
        if(parsedAccessTokenInfo == null) {
            throw new GeneralException(ResponseCode.FORBIDDEN);
        }

        //RT 파싱. 만료, 잘못된 토큰은 예외
        RefreshTokenInfo parsedRefreshTokenInfo = jwtProvider.parseRefreshToken(refreshToken);

        //발급한 AT, RT 두 쌍이 맞는지, 폐기된 토큰은 아닌지 db를 이용해 검증
        //해당 AT가 RT와 일치 여부에 상관 없이 조회화 동시에 무조건 삭제됨
        boolean validateDb = jwtStorageService.checkPresentAndDelete(parsedAccessTokenInfo, parsedRefreshTokenInfo);
        if(!validateDb) {
            //검증 실패
            throw new GeneralException(ResponseCode.FORBIDDEN);
        }

        //검증 성공, 토큰 재발급
        Long userId = parsedRefreshTokenInfo.getUserId();
        issueToken(response, userId);
    }
}
