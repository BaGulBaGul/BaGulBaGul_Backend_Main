package com.BaGulBaGul.BaGulBaGul.domain.admin.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.AdminManagePasswordLoginUserJoinRequest;

public interface AMPWAdminService {
    Long registerAMPWUserAndGetUserId(AdminManagePasswordLoginUserJoinRequest ampwJoinRequest);
}
