package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.AdminManageEventHostUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.AdminManageEventHostUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminManageEventHostUserServiceImpl implements AdminManageEventHostUserService {

    private final AdminManageEventHostUserRepository adminManageEventHostUserRepository;

    @Override
    @Transactional
    public AdminManageEventHostUser registerAdminManageEventHostUser(User user) {
        adminManageEventHostUserRepository.save(
                AdminManageEventHostUser.builder()
                        .user(user)
                        .build()
        );
        return null;
    }

    @Override
    @Transactional
    public void deRegisterAdminManageEventHostUser(Long adminManageEventHostUserId) {
        adminManageEventHostUserRepository.deleteById(adminManageEventHostUserId);
    }

    @Override
    @Transactional
    public void deRegisterAdminManageEventHostUserByUserId(Long userId) {
        adminManageEventHostUserRepository.deleteByUserId(userId);
    }
}
