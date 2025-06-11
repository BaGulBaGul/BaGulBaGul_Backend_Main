package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.UserModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.MyUserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.OtherUserInfoResponse;

public interface UserInfoService {
    MyUserInfoResponse getMyUserInfo(Long userId);
    OtherUserInfoResponse getOtherUserInfo(Long userId);
    void modifyUserInfo(UserModifyRequest userModifyRequest, Long userId);
}
