package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.AdminManagePasswordLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.PasswordLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.AdminManagePasswordLoginUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminManagePasswordLoginUserServiceImpl implements AdminManagePasswordLoginUserService {

    private final AdminManagePasswordLoginUserRepository adminManagePasswordLoginUserRepository;

    @Override
    @Transactional
    public AdminManagePasswordLoginUser registerAdminManagePasswordLoginUser(PasswordLoginUser passwordLoginUser) {
        AdminManagePasswordLoginUser adminManagePasswordLoginUser = adminManagePasswordLoginUserRepository.save(
                AdminManagePasswordLoginUser.builder()
                .passwordLoginUser(passwordLoginUser)
                .build()
        );
        return adminManagePasswordLoginUser;
    }

    @Override
    @Transactional
    public void deRegisterAdminManagePasswordLoginUser(Long adminManagePasswordLoginUserId) {
        adminManagePasswordLoginUserRepository.deleteById(adminManagePasswordLoginUserId);
    }
}
