package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.AdminManagePasswordLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.PasswordLoginUser;

public interface AdminManagePasswordLoginUserService {
    AdminManagePasswordLoginUser registerAdminManagePasswordLoginUser(PasswordLoginUser passwordLoginUser);
    void deRegisterAdminManagePasswordLoginUser(Long adminManagePasswordLoginUserId);
}
