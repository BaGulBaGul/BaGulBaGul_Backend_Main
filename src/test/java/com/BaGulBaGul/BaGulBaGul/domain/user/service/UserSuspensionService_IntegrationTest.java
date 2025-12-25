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
import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import org.junit.jupiter.api.BeforeEach;
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

    //정상 유저
    User normalUser;

    //이미 정지된 유저
    User suspendedUser;
    String suspendedUserReason;
    LocalDateTime suspendedUserEndDate;

    //정지가 만료된 유저
    User suspendExpiredUser;
    String suspendedExpiredUserReason;
    LocalDateTime suspendedExpiredUserEndDate;

    //대상 Admin 유저
    User targetAdminUser;
    //요청자 Admin 유저
    User requestAdminUser;

    @BeforeEach
    void init() {
        normalUser = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        requestAdminUser = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());
        targetAdminUser = userJoinService.registerUser(UserSample.getAdmin2UserRegisterRequest());

        suspendedUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
        suspendedUserReason = "test reason";
        suspendedUserEndDate = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusDays(7);

        suspendExpiredUser = userJoinService.registerUser(UserSample.getNormal3UserRegisterRequest());
        suspendedExpiredUserReason = "test reason2";
        suspendedExpiredUserEndDate = LocalDateTime.now().minusDays(1); // 과거 날짜로 설정

        userSuspensionService.suspendUser(
                requestAdminUser.getId(),
                suspendedUser.getId(),
                new SuspendUserRequest(suspendedUserReason, suspendedUserEndDate)
        );
        userSuspensionService.suspendUser(
                requestAdminUser.getId(),
                suspendExpiredUser.getId(),
                new SuspendUserRequest(suspendedExpiredUserReason, suspendedExpiredUserEndDate)
        );

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("정지되어 있지 않은 유저를 정지시킨다.")
    @Transactional
    void test_suspendUser_ifNotSuspended() {
        //given
        String reason = "test reason";
        LocalDateTime endDate = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusDays(7);

        //when
        userSuspensionService.suspendUser(
                requestAdminUser.getId(), normalUser.getId(), new SuspendUserRequest(reason, endDate));

        //then
        em.flush();
        em.clear();

        User suspendedUser = userRepository.findById(normalUser.getId()).get();
        assertThat(suspendedUser.isSuspended()).isTrue();

        UserSuspensionStatus status = userSuspensionStatusRepository.findById(normalUser.getId()).get();
        assertThat(status.getReason()).isEqualTo(reason);
        assertThat(status.getEndDate()).isEqualTo(endDate);
    }

    @Test
    @DisplayName("이미 정지된 유저의 정지 기간을 연장한다.")
    @Transactional
    void test_extendSuspension_ifAlreadySuspended() {
        //given
        String reason2 = "extended reason";
        LocalDateTime endDate2 = suspendedUserEndDate.plusDays(7);

        //when
        userSuspensionService.suspendUser(
                requestAdminUser.getId(), suspendedUser.getId(), new SuspendUserRequest(reason2, endDate2));

        //then
        em.flush();
        em.clear();

        //유저 엔티티의 정지 상태도 동기화되어야 한다
        suspendedUser = userRepository.findById(suspendedUser.getId()).get();
        assertThat(suspendedUser.isSuspended()).isTrue();

        //유저의 정지 상태 확인
        UserSuspensionStatus status = userSuspensionStatusRepository.findById(suspendedUser.getId()).get();
        assertThat(status.getReason()).isEqualTo(reason2);
        assertThat(status.getEndDate()).isEqualTo(endDate2);
    }

    @Test
    @DisplayName("정지 대상이 ADMIN이라면 예외")
    @Transactional
    void test_suspendUser_ifTargetUserIsAdmin() {
        //given
        String reason = "test reason";
        LocalDateTime endDate = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusDays(7);
        //when
        assertThrows(GeneralException.class, () -> {
            userSuspensionService.suspendUser(
                    requestAdminUser.getId(), targetAdminUser.getId(), new SuspendUserRequest(reason, endDate));
        });
    }

    @Test
    @DisplayName("유저의 정지를 해제한다.")
    @Transactional
    void test_liftSuspension_ifSuspended() {
        //when
        String liftReason = "lift reason";
        userSuspensionService.liftSuspension(
                requestAdminUser.getId(),
                suspendedUser.getId(),
                new LiftUserSuspensionRequest(liftReason)
        );

        //then
        em.flush();
        em.clear();

        //유저의 정지를 해제한 이후에는 유저 엔티티의 정지 상태도 동기화되어야 한다
        User unsuspendedUser = userRepository.findById(suspendedUser.getId()).get();
        assertThat(unsuspendedUser.isSuspended()).isFalse();

        //유저의 정지를 해제한 이후에는 정지 상태에서 정보가 사라져야 한다
        UserSuspensionStatus status = userSuspensionStatusRepository.findById(suspendedUser.getId()).get();
        assertThat(status.getEndDate()).isNull();
        assertThat(status.getReason()).isNull();
    }

    @Test
    @DisplayName("정지되지 않은 유저의 정지를 해제하려하면 예외가 발생한다.")
    @Transactional
    void should_throw_UserNotSuspendedException_when_liftingNotSuspended() {
        //when then
        String liftReason = "lift reason";
        assertThrows(UserNotSuspendedException.class, () -> {
            userSuspensionService.liftSuspension(requestAdminUser.getId(), normalUser.getId(), new LiftUserSuspensionRequest(liftReason));
        });
    }

    @Test
    @DisplayName("정지되지 않은 유저의 정지 상태를 확인한다.")
    @Transactional
    void test_getUserSuspensionStatus_ifNotSuspended() {
        //given
        //when
        UserSuspensionStatusResponse result = userSuspensionService.getUserSuspensionStatus(normalUser.getId());

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
        //when
        UserSuspensionStatusResponse result = userSuspensionService.getUserSuspensionStatus(suspendedUser.getId());

        //then
        assertThat(result.isSuspended()).isTrue();
        assertThat(result.getReason()).isEqualTo(suspendedUserReason);
        assertThat(result.getEndDate()).isEqualTo(suspendedUserEndDate);

        User checkedUser = userRepository.findById(suspendedUser.getId()).get();
        assertThat(checkedUser.isSuspended()).isTrue();
    }

    @Test
    @DisplayName("정지가 만료된 유저의 정지 상태를 확인한다")
    @Transactional
    void test_getUserSuspensionStatus_ifSuspensionExpired() {
        //given
        //when
        UserSuspensionStatusResponse result = userSuspensionService.getUserSuspensionStatus(
                suspendExpiredUser.getId());

        //then
        assertThat(result.isSuspended()).isFalse();
        assertThat(result.getReason()).isNull();
        assertThat(result.getEndDate()).isNull();

        em.flush();
        em.clear();

        //정지가 만료된 유저의 정지 상태를 조회한 이후에는 User엔티티의 정지 상태도 동기화되어야 한다
        User checkedUser = userRepository.findById(suspendExpiredUser.getId()).get();
        assertThat(checkedUser.isSuspended()).isFalse();

        //정지가 만료된 유저의 정지 상태를 조회한 이후에는 정지 상태에서 정보가 사라져야 한다
        UserSuspensionStatus status = userSuspensionStatusRepository.findById(suspendExpiredUser.getId()).get();
        assertThat(status.getEndDate()).isNull();
        assertThat(status.getReason()).isNull();
    }
}
