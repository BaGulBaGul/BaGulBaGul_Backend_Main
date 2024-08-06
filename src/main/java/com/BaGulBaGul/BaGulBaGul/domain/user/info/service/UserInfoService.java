package com.BaGulBaGul.BaGulBaGul.domain.user.info.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.MyUserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.OtherUserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.UserModifyRequest;

public interface UserInfoService {
    MyUserInfoResponse getMyUserInfo(Long userId);
    OtherUserInfoResponse getOtherUserInfo(Long userId);
    void modifyUserInfo(UserModifyRequest userModifyRequest, Long userId);
}
