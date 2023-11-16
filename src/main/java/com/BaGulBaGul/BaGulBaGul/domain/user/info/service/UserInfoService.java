package com.BaGulBaGul.BaGulBaGul.domain.user.info.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.UserModifyRequest;

public interface UserInfoService {
    void modifyUserInfo(UserModifyRequest userModifyRequest, Long userId);
}
