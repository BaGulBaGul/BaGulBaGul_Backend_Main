package com.BaGulBaGul.BaGulBaGul.domain.admin.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.service.response.UserSearchByAdminResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.SuspendUserRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserSearchRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserSuspensionService;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserAdminServiceImpl_IntegrationTest {

    @Autowired
    UserAdminService userAdminService;

    @Autowired
    UserJoinService userJoinService;

    @Autowired
    UserSuspensionService userSuspensionService;

    @Test
    @DisplayName("유저 검색 테스트 - 기본")
    void getUserPageByAdminSearch_base() {
        //given
        User user1 = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        User user2 = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
        User admin = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());
        LocalDateTime suspensionEndDate = LocalDateTime.now().plusDays(1).withNano(0);
        String suspensionReason = "test";
        userSuspensionService.suspendUser(
                admin.getId(),
                user2.getId(),
                SuspendUserRequest.builder()
                        .endDate(suspensionEndDate)
                        .reason(suspensionReason)
                        .build()
                );

        UserSearchRequest request = UserSearchRequest.builder().build();
        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<UserSearchByAdminResponse> result = userAdminService.getUserPageByAdminSearch(request, pageable);

        //then
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getContent()).hasSize(3);

        UserSearchByAdminResponse user1Info = result.getContent().stream().filter(x -> x.getId().equals(user1.getId())).findFirst().get();
        UserSearchByAdminResponse user2Info = result.getContent().stream().filter(x -> x.getId().equals(user2.getId())).findFirst().get();

        assertThat(user1Info.isSuspend()).isFalse();
        assertThat(user1Info.getSuspendEndDate()).isNull();
        assertThat(user1Info.getSuspensionReason()).isNull();
        assertThat(user2Info.isSuspend()).isTrue();
        assertThat(user2Info.getSuspendEndDate()).isEqualTo(suspensionEndDate);
        assertThat(user2Info.getSuspensionReason()).isEqualTo(suspensionReason);
    }
}
