package com.BaGulBaGul.BaGulBaGul.global.auth.service;

import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AccessTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.RefreshTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.ExpiredRefreshTokenException;

public interface JwtStorageService {
    /**
     * AT, RT 쌍을 redis에 저장한다.
     */
    void save(AccessTokenInfo accessTokenInfo, RefreshTokenInfo refreshTokenInfo) throws ExpiredRefreshTokenException;

    /**
     * AT, RT 쌍이 존재하는지 확인하고, 존재 여부에 상관없이 AT가 key인 값을 지운다.
     * @return AT가 key이고 RT가 value인 페어가 존재하는지 여부
     */
    boolean checkPresentAndDelete(AccessTokenInfo accessTokenInfo, RefreshTokenInfo refreshTokenInfo);

    /**
     * AT가 key인 페어를 지운다
     */
    void delete(AccessTokenInfo accessTokenInfo);
}
