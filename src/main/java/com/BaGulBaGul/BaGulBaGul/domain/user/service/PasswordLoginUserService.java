package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.PasswordLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.PasswordLoginUserRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.UserRegisterRequest;

public interface PasswordLoginUserService {
    PasswordLoginUser findPasswordLoginUser(String loginId, String loginPassword);
    PasswordLoginUser findPasswordLoginUser(String loginId, String loginPassword, String roleName);
    PasswordLoginUser registerPasswordLoginUser(
            PasswordLoginUserRegisterRequest passwordLoginUserRegisterRequest,
            User user
    );
    void deletePasswordLoginUser(String loginId);
}
