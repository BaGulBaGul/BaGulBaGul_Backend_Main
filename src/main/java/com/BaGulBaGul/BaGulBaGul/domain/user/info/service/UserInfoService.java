package com.BaGulBaGul.BaGulBaGul.domain.user.info.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.UserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.UserModifyRequest;

public interface UserInfoService {
    UserInfoResponse getUserInfo(Long userId);
    void modifyUserInfo(UserModifyRequest userModifyRequest, Long userId);
}
