package com.BaGulBaGul.BaGulBaGul.domain.admin.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.AMPWUserRegisterApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.AdminManagePasswordLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.PasswordLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.AdminManagePasswordLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.UserNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserInfoService;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AMPWAdminServiceImpl implements AMPWAdminService {

    private final UserJoinService userJoinService;

    @Override
    @Transactional
    public Long registerAMPWUserAndGetUserId(AdminManagePasswordLoginUserJoinRequest ampwJoinRequest) {
        AdminManagePasswordLoginUser adminManagePasswordLoginUser = userJoinService.joinAdminManagePasswordLoginUser(
                ampwJoinRequest);
        return adminManagePasswordLoginUser.getPasswordLoginUser().getUser().getId();
    }
}
