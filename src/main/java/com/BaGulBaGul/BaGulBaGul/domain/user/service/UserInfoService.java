package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.MyUserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.OtherUserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.UserModifyRequest;

public interface UserInfoService {
    MyUserInfoResponse getMyUserInfo(Long userId);
    OtherUserInfoResponse getOtherUserInfo(Long userId);
    void modifyUserInfo(UserModifyRequest userModifyRequest, Long userId);
}
