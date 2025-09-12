package com.BaGulBaGul.BaGulBaGul.domain.admin.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserSearchRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.service.response.UserSearchByAdminResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.querydsl.FindUserByCondition;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.querydsl.FindUserByCondition.UserIdsWithTotalCount;
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

    private final FindUserByCondition findUserByCondition;
    private final UserRepository userRepository;
    private final UserSuspensionService userSuspensionService;

    @Override
    @Transactional
    public Page<UserSearchByAdminResponse> getUserPageByAdminSearch(
            UserSearchRequest userSearchRequest, Pageable pageable
    ) {
        UserIdsWithTotalCount result = findUserByCondition.getUserIdsByCondition(userSearchRequest,
                pageable);
        List<UserSearchByAdminResponse> responses = getUserInfoByAdminSearchResponses(result.getEventIds());
        return new PageImpl<>(responses, pageable, result.getTotalCount());
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
