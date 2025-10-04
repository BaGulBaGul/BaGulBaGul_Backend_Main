package com.BaGulBaGul.BaGulBaGul.domain.recruitment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.BaGulBaGul.BaGulBaGul.domain.event.sampledata.EventSample;
import com.BaGulBaGul.BaGulBaGul.domain.event.service.EventService;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostService;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.constant.RecruitmentState;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request.RecruitmentConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request.RecruitmentModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request.RecruitmentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response.RecruitmentSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.exception.RecruitmentNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.RecruitmentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.sampledata.RecruitmentSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.GeneralRoleType;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import com.BaGulBaGul.BaGulBaGul.global.exception.NoPermissionException;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class RecruitmentService_IntegrationTest {

    @Autowired
    RecruitmentService recruitmentService;

    @Autowired
    EventService eventService;

    @SpyBean
    PostService postService;

    @Autowired
    UserJoinService userJoinService;

    @Autowired
    RecruitmentRepository recruitmentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Nested
    @DisplayName("모집글 등록 테스트")
    class RecruitmentRegisterTest {

        @Test
        @DisplayName("정상 등록")
        @Transactional
        void shouldOK() {
            //given
            User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            AuthenticatedUserInfo authenticatedUserInfo = AuthenticatedUserInfo.builder()
                    .userId(user.getId())
                    .roles(List.of(GeneralRoleType.EVENT_HOST.name()))
                    .build();
            User eventHostUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            Long eventId = eventService.registerEvent(
                    authenticatedUserInfo,
                    EventSample.getNormalRegisterRequest(eventHostUser.getId())
            );

            RecruitmentRegisterRequest recruitmentRegisterRequest = RecruitmentSample.getNormalRegisterRequest();

            //when
            Long recruitmentId = recruitmentService.registerRecruitment(authenticatedUserInfo, eventId,
                    recruitmentRegisterRequest);
            Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElse(null);

            //then
            assertThat(recruitment.getState()).isEqualTo(RecruitmentState.PROCEEDING);
            assertThat(recruitment.getDeleted()).isEqualTo(false);
            assertThat(recruitment.getEvent().getId()).isEqualTo(eventId);
            //참가자
            assertThat(recruitment.getCurrentHeadCount()).isEqualTo(recruitmentRegisterRequest
                    .getParticipantStatusRegisterRequest().getCurrentHeadCount());
            assertThat(recruitment.getMaxHeadCount()).isEqualTo(recruitmentRegisterRequest
                    .getParticipantStatusRegisterRequest().getMaxHeadCount());
            //기간
            assertThat(recruitment.getStartDate()).isEqualTo(recruitmentRegisterRequest
                    .getPeriodRegisterRequest().getStartDate());
            assertThat(recruitment.getEndDate()).isEqualTo(recruitmentRegisterRequest
                    .getPeriodRegisterRequest().getEndDate());
            //게시글 등록 서비스를 호출했는지(이벤트, 모집글 두번)
            verify(postService, times(2)).registerPost(eq(user), any());
        }
    }

    @Nested
    @DisplayName("모집글 수정 테스트")
    class RecruitmentModifyTest {

        @Test
        @DisplayName("전부 수정해야 함")
        @Transactional
        void shouldChangeAll() {
            //given
            User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            AuthenticatedUserInfo authenticatedUserInfo = AuthenticatedUserInfo.builder()
                    .userId(user.getId())
                    .roles(List.of(GeneralRoleType.EVENT_HOST.name()))
                    .build();
            User eventHostUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            Long eventId = eventService.registerEvent(
                    authenticatedUserInfo,
                    EventSample.getNormalRegisterRequest(eventHostUser.getId())
            );

            RecruitmentRegisterRequest recruitmentRegisterRequest = RecruitmentSample.getNormalRegisterRequest();
            RecruitmentModifyRequest recruitmentModifyRequest = RecruitmentSample.getNormal2ModifyRequest();
            Long recruitmentId = recruitmentService.registerRecruitment(
                    authenticatedUserInfo, eventId, recruitmentRegisterRequest);

            //when
            recruitmentService.modifyRecruitment(authenticatedUserInfo, recruitmentId, recruitmentModifyRequest);
            entityManager.flush();
            entityManager.clear();

            Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElse(null);

            //then
            assertThat(recruitment.getState()).isEqualTo(RecruitmentState.PROCEEDING);
            assertThat(recruitment.getDeleted()).isEqualTo(false);
            assertThat(recruitment.getEvent().getId()).isEqualTo(eventId);
            //참가자
            assertThat(recruitment.getCurrentHeadCount()).isEqualTo(recruitmentModifyRequest
                    .getParticipantStatusModifyRequest().getCurrentHeadCount().get());
            assertThat(recruitment.getMaxHeadCount()).isEqualTo(recruitmentModifyRequest
                    .getParticipantStatusModifyRequest().getMaxHeadCount().get());
            //기간
            assertThat(recruitment.getStartDate()).isEqualTo(recruitmentModifyRequest
                    .getPeriodModifyRequest().getStartDate().get());
            assertThat(recruitment.getEndDate()).isEqualTo(recruitmentModifyRequest
                    .getPeriodModifyRequest().getEndDate().get());
            //게시글 서비스에 수정요청을 보냈는지
            verify(postService, times(1)).modifyPost(any(), any());
        }

        @Test
        @DisplayName("아무것도 수정하지 말아야 함")
        @Transactional
        void shouldChangeNothing() {
            //given
            User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            AuthenticatedUserInfo authenticatedUserInfo = AuthenticatedUserInfo.builder()
                    .userId(user.getId())
                    .roles(List.of(GeneralRoleType.EVENT_HOST.name()))
                    .build();
            User eventHostUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            Long eventId = eventService.registerEvent(
                    authenticatedUserInfo,
                    EventSample.getNormalRegisterRequest(eventHostUser.getId())
            );

            RecruitmentRegisterRequest recruitmentRegisterRequest = RecruitmentSample.getNormalRegisterRequest();

            RecruitmentModifyRequest recruitmentModifyRequest = RecruitmentModifyRequest.builder()
                    .build();
            Long recruitmentId = recruitmentService.registerRecruitment(authenticatedUserInfo, eventId,
                    recruitmentRegisterRequest);

            //when
            recruitmentService.modifyRecruitment(authenticatedUserInfo, recruitmentId, recruitmentModifyRequest);
            entityManager.flush();
            entityManager.clear();

            Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElse(null);

            //then
            assertThat(recruitment.getState()).isEqualTo(RecruitmentSample.NORMAL_RECRUITMENT_STATE);
            assertThat(recruitment.getDeleted()).isEqualTo(false);
            assertThat(recruitment.getEvent().getId()).isEqualTo(eventId);
            //참가자
            assertThat(recruitment.getMaxHeadCount()).isEqualTo(recruitmentRegisterRequest
                    .getParticipantStatusRegisterRequest().getMaxHeadCount());
            //기간
            assertThat(recruitment.getStartDate()).isEqualTo(recruitmentRegisterRequest
                    .getPeriodRegisterRequest().getStartDate());
            assertThat(recruitment.getEndDate()).isEqualTo(recruitmentRegisterRequest
                    .getPeriodRegisterRequest().getEndDate());
            //게시글 서비스에 수정요청을 보냈는지
            verify(postService, times(1)).modifyPost(any(), any());
        }
    }
    @Nested
    @DisplayName("권한 테스트")
    class RecruitmentPermissionTest {
        @Test
        @DisplayName("관리자는 다른 사람의 모집글을 수정할 수 있다.")
        @Transactional
        void given_admin_can_modify_others_recruitment() {
            //given
            User writer = userJoinService.registerUser(UserSample.getEventHostUserRegisterRequest());
            User admin = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());
            AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
            AuthenticatedUserInfo adminInfo = new AuthenticatedUserInfo(admin.getId(), List.of(GeneralRoleType.ADMIN.name()));

            Long eventId = eventService.registerEvent(writerInfo, EventSample.getNormalRegisterRequest(writer.getId()));
            Long recruitmentId = recruitmentService.registerRecruitment(writerInfo, eventId, RecruitmentSample.getNormalRegisterRequest());

            RecruitmentModifyRequest recruitmentModifyRequest = RecruitmentSample.getNormal2ModifyRequest();

            //when
            recruitmentService.modifyRecruitment(adminInfo, recruitmentId, recruitmentModifyRequest);

            //then
            entityManager.flush();
            entityManager.clear();
            Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElse(null);
            assertThat(recruitment.getMaxHeadCount()).isEqualTo(recruitmentModifyRequest.getParticipantStatusModifyRequest().getMaxHeadCount().get());
        }

        @Test
        @DisplayName("관리자는 다른 사람의 모집글을 삭제할 수 있다.")
        @Transactional
        void given_admin_can_delete_others_recruitment() {
            //given
            User writer = userJoinService.registerUser(UserSample.getEventHostUserRegisterRequest());
            User admin = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());
            AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
            AuthenticatedUserInfo adminInfo = new AuthenticatedUserInfo(admin.getId(), List.of(GeneralRoleType.ADMIN.name()));

            Long eventId = eventService.registerEvent(writerInfo, EventSample.getNormalRegisterRequest(writer.getId()));
            Long recruitmentId = recruitmentService.registerRecruitment(writerInfo, eventId, RecruitmentSample.getNormalRegisterRequest());

            //when
            recruitmentService.deleteRecruitment(adminInfo, recruitmentId);

            //then
            entityManager.flush();
            entityManager.clear();
            Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElse(null);
            assertThat(recruitment.getDeleted()).isTrue();
        }

        @Test
        @DisplayName("일반유저는 다른 사람의 모집글을 수정할 수 없다.")
        @Transactional
        void given_normal_user_cannot_modify_others_recruitment() {
            //given
            User writer = userJoinService.registerUser(UserSample.getEventHostUserRegisterRequest());
            User normalUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
            AuthenticatedUserInfo normalUserInfo = new AuthenticatedUserInfo(normalUser.getId(), List.of(GeneralRoleType.USER.name()));

            Long eventId = eventService.registerEvent(writerInfo, EventSample.getNormalRegisterRequest(writer.getId()));
            Long recruitmentId = recruitmentService.registerRecruitment(writerInfo, eventId, RecruitmentSample.getNormalRegisterRequest());

            RecruitmentModifyRequest recruitmentModifyRequest = RecruitmentSample.getNormal2ModifyRequest();

            //when
            //then
            assertThrows(NoPermissionException.class, () -> {
                recruitmentService.modifyRecruitment(normalUserInfo, recruitmentId, recruitmentModifyRequest);
            });
        }

        @Test
        @DisplayName("일반유저는 다른 사람의 모집글을 삭제할 수 없다.")
        @Transactional
        void given_normal_user_cannot_delete_others_recruitment() {
            //given
            User writer = userJoinService.registerUser(UserSample.getEventHostUserRegisterRequest());
            User normalUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
            AuthenticatedUserInfo normalUserInfo = new AuthenticatedUserInfo(normalUser.getId(), List.of(GeneralRoleType.USER.name()));

            Long eventId = eventService.registerEvent(writerInfo, EventSample.getNormalRegisterRequest(writer.getId()));
            Long recruitmentId = recruitmentService.registerRecruitment(writerInfo, eventId, RecruitmentSample.getNormalRegisterRequest());

            //when
            //then
            assertThrows(NoPermissionException.class, () -> {
                recruitmentService.deleteRecruitment(normalUserInfo, recruitmentId);
            });
        }
    }

    @Nested
    @DisplayName("조회 테스트")
    class RecruitmentReadTest {

        User user;
        AuthenticatedUserInfo authenticatedUserInfo;
        Long eventId;

        @BeforeEach
        void setUp() {
            user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            authenticatedUserInfo = AuthenticatedUserInfo.builder()
                    .userId(user.getId())
                    .roles(List.of(GeneralRoleType.EVENT_HOST.name()))
                    .build();
            User eventHostUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            eventId = eventService.registerEvent(
                    authenticatedUserInfo,
                    EventSample.getNormalRegisterRequest(eventHostUser.getId())
            );
        }

        @Test
        @DisplayName("삭제된 모집글 단일 상세 조회 테스트")
        @Transactional
        void getRecruitmentDetailById_deleted() {
            //given
            RecruitmentRegisterRequest recruitmentRegisterRequest = RecruitmentSample.getNormalRegisterRequest();
            Long recruitmentId = recruitmentService.registerRecruitment(authenticatedUserInfo, eventId, recruitmentRegisterRequest);
            recruitmentService.deleteRecruitment(authenticatedUserInfo, recruitmentId);
            entityManager.flush();
            entityManager.clear();
            //when
            //then
            assertThrows(RecruitmentNotFoundException.class, () -> {
                recruitmentService.getRecruitmentDetailResponseById(recruitmentId);
            });
        }

        @Test
        @DisplayName("모집글 페이지 조회 - 삭제된 모집글은 보이지 않아야 함")
        @Transactional
        void getRecruitmentPageByCondition_deleted() {
            //given
            RecruitmentRegisterRequest recruitmentRegisterRequest1 = RecruitmentSample.getNormalRegisterRequest();
            Long recruitmentId1 = recruitmentService.registerRecruitment(authenticatedUserInfo, eventId, recruitmentRegisterRequest1);
            RecruitmentRegisterRequest recruitmentRegisterRequest2 = RecruitmentSample.getNormalRegisterRequest();
            Long recruitmentId2 = recruitmentService.registerRecruitment(authenticatedUserInfo, eventId, recruitmentRegisterRequest2);

            recruitmentService.deleteRecruitment(authenticatedUserInfo, recruitmentId2);

            //when
            Page<RecruitmentSimpleResponse> page = recruitmentService.getRecruitmentPageByCondition(
                    new RecruitmentConditionalRequest(), PageRequest.of(0, 10));

            //then
            assertThat(page.getContent().size()).isEqualTo(1);
            assertThat(page.getContent().get(0).getRecruitment().getRecruitmentId()).isEqualTo(recruitmentId1);
        }
    }
}