package com.BaGulBaGul.BaGulBaGul.domain.admin.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.AdminManageEventHostUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.AdminManagePasswordLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.AdminManageEventHostUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.AdminManagePasswordLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserSearchRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.service.response.UserSearchByAdminResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.querydsl.FindUserByCondition.UserIdsWithTotalCount;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserSuspensionService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {

    private final UserRepository userRepository;
    private final UserSuspensionService userSuspensionService;
    private final UserJoinService userJoinService;

    @Override
    @Transactional
    public Page<UserSearchByAdminResponse> getUserPageByAdminSearch(
            UserSearchRequest userSearchRequest, Pageable pageable
    ) {
        UserIdsWithTotalCount result = userRepository.getUserIdsByCondition(userSearchRequest,
                pageable);
        List<UserSearchByAdminResponse> responses = getUserInfoByAdminSearchResponses(result.getUserIds());
        return new PageImpl<>(responses, pageable, result.getTotalCount());
    }

    @Override
    @Transactional
    public Long registerAMEHUserAndGetUserId(AdminManageEventHostUserJoinRequest adminManageEventHostUserJoinRequest) {
        AdminManageEventHostUser adminManageEventHostUser = userJoinService.joinAdminManageEventHostUser(
                adminManageEventHostUserJoinRequest);
        return adminManageEventHostUser.getUser().getId();
    }
    @Override
    @Transactional
    public Long registerAMPWUserAndGetUserId(AdminManagePasswordLoginUserJoinRequest ampwJoinRequest) {
        AdminManagePasswordLoginUser adminManagePasswordLoginUser = userJoinService.joinAdminManagePasswordLoginUser(
                ampwJoinRequest);
        return adminManagePasswordLoginUser.getPasswordLoginUser().getUser().getId();
    }

    private List<UserSearchByAdminResponse> getUserInfoByAdminSearchResponses(List<Long> userIds) {
        //패치 조인
        userRepository.findUserWithRolesAndSuspensionStatusByIds(userIds);
        //순서 유지한 채로 Response로 변환
        return userIds.stream()
                .map(userId -> UserSearchByAdminResponse.from(
                        userRepository.findById(userId).get(),
                        userSuspensionService.getUserSuspensionStatus(userId)))
                .collect(Collectors.toList());
    }
}
