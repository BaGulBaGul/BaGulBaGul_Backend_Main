package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.AdminManageEventHostUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;

public interface AdminManageEventHostUserService {
    AdminManageEventHostUser registerAdminManageEventHostUser(User user);
    void deRegisterAdminManageEventHostUser(Long adminManageEventHostUserId);
    void deRegisterAdminManageEventHostUserByUserId(Long userId);
}
