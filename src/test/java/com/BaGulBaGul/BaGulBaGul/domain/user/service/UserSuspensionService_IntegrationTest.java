package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.UserSuspensionStatus;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.LiftUserSuspensionRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.SuspendUserRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.UserSuspensionStatusResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.UserNotSuspendedException;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserSuspensionStatusRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserSuspensionService_IntegrationTest {

    @Autowired
    UserSuspensionService userSuspensionService;
    @Autowired
    UserJoinService userJoinService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserSuspensionStatusRepository userSuspensionStatusRepository;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("정지되어 있지 않은 유저를 정지시킨다.")
    @Transactional
    void test_suspendUser_ifNotSuspended() {
        //given
        User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        User admin = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());
        String reason = "test reason";
        LocalDateTime endDate = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusDays(7);

        //when
        userSuspensionService.suspendUser(admin.getId(), user.getId(), new SuspendUserRequest(reason, endDate));

        //then
        em.flush();
        em.clear();

        User suspendedUser = userRepository.findById(user.getId()).get();
        assertThat(suspendedUser.isSuspended()).isTrue();

        UserSuspensionStatus status = userSuspensionStatusRepository.findById(user.getId()).get();
        assertThat(status.getReason()).isEqualTo(reason);
        assertThat(status.getEndDate()).isEqualTo(endDate);
    }

    @Test
    @DisplayName("이미 정지된 유저의 정지 기간을 연장한다.")
    @Transactional
    void test_extendSuspension_ifAlreadySuspended() {
        //given
        User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        User admin = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());
        String reason1 = "test reason";
        LocalDateTime endDate1 = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusDays(7);
        userSuspensionService.suspendUser(admin.getId(), user.getId(), new SuspendUserRequest(reason1, endDate1));

        String reason2 = "extended reason";
        LocalDateTime endDate2 = endDate1.plusDays(7);

        //when
        userSuspensionService.suspendUser(admin.getId(), user.getId(), new SuspendUserRequest(reason2, endDate2));

        //then
        em.flush();
        em.clear();

        User suspendedUser = userRepository.findById(user.getId()).get();
        assertThat(suspendedUser.isSuspended()).isTrue();

        UserSuspensionStatus status = userSuspensionStatusRepository.findById(user.getId()).get();
        assertThat(status.getReason()).isEqualTo(reason2);
        assertThat(status.getEndDate()).isEqualTo(endDate2);
    }
    @Test
    @DisplayName("유저의 정지를 해제한다.")
    @Transactional
    void test_liftSuspension_ifSuspended() {
        //given
        User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        User admin = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());
        String reason = "test reason";
        LocalDateTime endDate = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusDays(7);
        userSuspensionService.suspendUser(admin.getId(), user.getId(), new SuspendUserRequest(reason, endDate));

        String liftReason = "lift reason";

        //when
        userSuspensionService.liftSuspension(admin.getId(), user.getId(), new LiftUserSuspensionRequest(liftReason));

        //then
        em.flush();
        em.clear();

        User unsuspendedUser = userRepository.findById(user.getId()).get();
        assertThat(unsuspendedUser.isSuspended()).isFalse();

        UserSuspensionStatus status = userSuspensionStatusRepository.findById(user.getId()).get();
        assertThat(status.getEndDate()).isNull();
        assertThat(status.getReason()).isNull();
    }

    @Test
    @DisplayName("정지되지 않은 유저의 정지를 해제하려하면 예외가 발생한다.")
    @Transactional
    void should_throw_UserNotSuspendedException_when_liftingNotSuspended() {
        //given
        User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        User admin = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());
        String liftReason = "lift reason";

        //when
        //then
        assertThrows(UserNotSuspendedException.class, () -> {
            userSuspensionService.liftSuspension(admin.getId(), user.getId(), new LiftUserSuspensionRequest(liftReason));
        });
    }

    @Test
    @DisplayName("정지되지 않은 유저의 정지 상태를 확인한다.")
    @Transactional
    void test_getUserSuspensionStatus_ifNotSuspended() {
        //given
        User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());

        //when
        UserSuspensionStatusResponse result = userSuspensionService.getUserSuspensionStatus(user.getId());

        //then
        assertThat(result.isSuspended()).isFalse();
        assertThat(result.getReason()).isNull();
        assertThat(result.getEndDate()).isNull();
    }

    @Test
    @DisplayName("현재 정지 중인 유저의 정지 상태를 확인한다.")
    @Transactional
    void test_getUserSuspensionStatus_ifCurrentlySuspended() {
        //given
        User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        User admin = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());
        String reason = "test reason";
        LocalDateTime endDate = LocalDateTime.now().plusDays(7).withNano(0);
        userSuspensionService.suspendUser(admin.getId(), user.getId(), new SuspendUserRequest(reason, endDate));

        em.flush();
        em.clear();

        //when
        UserSuspensionStatusResponse result = userSuspensionService.getUserSuspensionStatus(user.getId());

        //then
        assertThat(result.isSuspended()).isTrue();
        assertThat(result.getReason()).isEqualTo(reason);
        assertThat(result.getEndDate()).isEqualTo(endDate);

        User checkedUser = userRepository.findById(user.getId()).get();
        assertThat(checkedUser.isSuspended()).isTrue();
    }

    @Test
    @DisplayName("정지가 만료된 유저의 정지 상태를 확인한다")
    @Transactional
    void test_getUserSuspensionStatus_ifSuspensionExpired() {
        //given
        User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        User admin = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());
        String reason = "test reason";
        LocalDateTime endDate = LocalDateTime.now().minusDays(1); // 과거 날짜로 설정
        userSuspensionService.suspendUser(admin.getId(), user.getId(), new SuspendUserRequest(reason, endDate));

        em.flush();
        em.clear();

        //when
        UserSuspensionStatusResponse result = userSuspensionService.getUserSuspensionStatus(user.getId());

        //then
        assertThat(result.isSuspended()).isFalse();
        assertThat(result.getReason()).isNull();
        assertThat(result.getEndDate()).isNull();

        em.flush();
        em.clear();

        User checkedUser = userRepository.findById(user.getId()).get();
        assertThat(checkedUser.isSuspended()).isFalse();

        UserSuspensionStatus status = userSuspensionStatusRepository.findById(user.getId()).get();
        assertThat(status.getEndDate()).isNull();
        assertThat(status.getReason()).isNull();
    }
}
