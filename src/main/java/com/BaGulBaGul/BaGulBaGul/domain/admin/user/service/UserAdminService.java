package com.BaGulBaGul.BaGulBaGul.domain.admin.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.AdminManageEventHostUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.AdminManagePasswordLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserSearchRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.service.response.UserSearchByAdminResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserAdminService {
    Page<UserSearchByAdminResponse> getUserPageByAdminSearch(
            UserSearchRequest userSearchRequest,
            Pageable pageable
    );

    Long registerAMEHUserAndGetUserId(AdminManageEventHostUserJoinRequest amehUserJoinRequest);
    Long registerAMPWUserAndGetUserId(AdminManagePasswordLoginUserJoinRequest ampwJoinRequest);
}
