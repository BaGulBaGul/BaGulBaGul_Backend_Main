package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.UserSuspensionStatus;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.LiftUserSuspensionRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.SuspendUserRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.InvalidSuspensionRequestException;
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
    @DisplayName("정지 종료일이 과거이면 예외가 발생한다.")
    @Transactional
    void should_throw_InvalidSuspensionUpdateException_when_endDateInPast() {
        //given
        User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        User admin = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());
        String reason = "test reason";
        LocalDateTime endDate = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).minusDays(7);

        //when
        //then
        assertThrows(InvalidSuspensionRequestException.class, () -> {
            userSuspensionService.suspendUser(admin.getId(), user.getId(), new SuspendUserRequest(reason, endDate));
        });
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
}
