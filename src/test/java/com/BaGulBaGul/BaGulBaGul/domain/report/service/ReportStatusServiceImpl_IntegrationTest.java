package com.BaGulBaGul.BaGulBaGul.domain.report.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventRepository;
import com.BaGulBaGul.BaGulBaGul.domain.event.sampledata.EventSample;
import com.BaGulBaGul.BaGulBaGul.domain.event.service.EventCommentService;
import com.BaGulBaGul.BaGulBaGul.domain.event.service.EventService;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostComment;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentChild;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentChildRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentChildRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostCommentService;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request.RecruitmentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.RecruitmentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.sampledata.RecruitmentSample;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.service.RecruitmentService;
import com.BaGulBaGul.BaGulBaGul.domain.report.CommentChildReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.CommentReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.EventReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.RecruitmentReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportStatusState;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.request.CompleteReportStatusRequest;
import com.BaGulBaGul.BaGulBaGul.domain.report.exception.ReportStatusNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.report.repository.CommentChildReportStatusRepository;
import com.BaGulBaGul.BaGulBaGul.domain.report.repository.CommentReportStatusRepository;
import com.BaGulBaGul.BaGulBaGul.domain.report.repository.EventReportStatusRepository;
import com.BaGulBaGul.BaGulBaGul.domain.report.repository.RecruitmentReportStatusRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.SuspendUserRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.UserSuspensionStatusResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserSuspensionService;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.GeneralRoleType;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("신고 상태 서비스 통합 테스트")
class ReportStatusServiceImpl_IntegrationTest {

    @Autowired
    ReportStatusService reportStatusService;
    @Autowired
    UserJoinService userJoinService;
    @Autowired
    EventService eventService;
    @Autowired
    RecruitmentService recruitmentService;
    @Autowired
    PostCommentService postCommentService;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    RecruitmentRepository recruitmentRepository;
    @Autowired
    EventReportStatusRepository eventReportStatusRepository;
    @Autowired
    RecruitmentReportStatusRepository recruitmentReportStatusRepository;
    @Autowired
    CommentReportStatusRepository commentReportStatusRepository;
    @Autowired
    CommentChildReportStatusRepository commentChildReportStatusRepository;
    @Autowired
    UserSuspensionService userSuspensionService;
    @Autowired
    EventCommentService eventCommentService;
    @Autowired
    EntityManager em;

    @Nested
    @DisplayName("이벤트 신고 상태 완료 테스트")
    class CompleteEventReportStatusTest {
        User admin;
        User writer;
        Event event;
        EventReportStatus eventReportStatus;
        AuthenticatedUserInfo adminInfo;

        @BeforeEach
        void init() {
            admin = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());
            writer = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            adminInfo = new AuthenticatedUserInfo(admin.getId(), List.of(GeneralRoleType.ADMIN.name()));

            AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(writer.getId());
            Long eventId = eventService.registerEvent(writerInfo, eventRegisterRequest);
            event = em.find(Event.class, eventId);

            eventReportStatus = eventReportStatusRepository.save(
                    EventReportStatus.builder()
                            .event(event)
                            .build()
            );
            em.flush();
            em.clear();
        }

        @Test
        @DisplayName("존재하지 않는 신고 상태라면 예외 발생")
        void reportStatus_not_exist() {
            //given
            Long wrongId = -1L;
            CompleteReportStatusRequest completeReportStatusRequest = CompleteReportStatusRequest.builder().build();

            //when
            //then
            assertThrows(ReportStatusNotExistException.class, () -> {
                reportStatusService.completeReportStatus(adminInfo, wrongId, completeReportStatusRequest);
            });
        }

        @Test
        @DisplayName("신고 대상 컨텐츠를 삭제하면 soft delete 되고 상태가 ACCEPTED가 된다")
        void deleteTargetContent_true() {
            //given
            CompleteReportStatusRequest completeReportStatusRequest = CompleteReportStatusRequest.builder()
                    .deleteTargetContent(true)
                    .build();

            //when
            reportStatusService.completeReportStatus(adminInfo, eventReportStatus.getId(), completeReportStatusRequest);

            //then
            em.flush();
            em.clear();
            //event가 soft delete 되었는지 확인
            Event resultEvent = eventRepository.findByIdIfNotDeleted(event.getId()).orElse(null);
            assertThat(resultEvent).isNull();
            //report status가 변경되었는지 확인
            EventReportStatus resultReportStatus = eventReportStatusRepository.findById(eventReportStatus.getId()).get();
            assertThat(resultReportStatus.getState()).isEqualTo(ReportStatusState.ACCEPTED);
            assertThat(resultReportStatus.isReportedContentDeleted()).isTrue();
            assertThat(resultReportStatus.isReportedContentWriterSuspended()).isFalse();
        }

        @Test
        @DisplayName("신고 대상 작성자를 정지시키면 정지되고 상태가 ACCEPTED가 된다")
        void suspendUser_true() {
            //given
            LocalDateTime now = LocalDateTime.now();
            CompleteReportStatusRequest completeReportStatusRequest = CompleteReportStatusRequest.builder()
                    .suspendUserRequest(
                            new SuspendUserRequest("test", now.plusDays(1))
                    )
                    .build();

            //when
            reportStatusService.completeReportStatus(adminInfo, eventReportStatus.getId(), completeReportStatusRequest);

            //then
            em.flush();
            em.clear();
            //유저가 정지되었는지 확인
            UserSuspensionStatusResponse suspensionStatus = userSuspensionService.getUserSuspensionStatus(writer.getId());
            assertThat(suspensionStatus.isSuspended()).isTrue();
            //report status가 변경되었는지 확인
            EventReportStatus resultReportStatus = eventReportStatusRepository.findById(eventReportStatus.getId()).get();
            assertThat(resultReportStatus.getState()).isEqualTo(ReportStatusState.ACCEPTED);
            assertThat(resultReportStatus.isReportedContentDeleted()).isFalse();
            assertThat(resultReportStatus.isReportedContentWriterSuspended()).isTrue();
        }

        @Test
        @DisplayName("둘 다 true이면 둘 다 처리되고 상태가 ACCEPTED가 된다")
        void deleteTargetContent_and_suspendUser_true() {
            //given
            LocalDateTime now = LocalDateTime.now();
            CompleteReportStatusRequest completeReportStatusRequest = CompleteReportStatusRequest.builder()
                    .deleteTargetContent(true)
                    .suspendUserRequest(
                            new SuspendUserRequest("test", now.plusDays(1))
                    )
                    .build();

            //when
            reportStatusService.completeReportStatus(adminInfo, eventReportStatus.getId(), completeReportStatusRequest);

            //then
            em.flush();
            em.clear();
            //event가 soft delete 되었는지 확인
            Event resultEvent = eventRepository.findByIdIfNotDeleted(event.getId()).orElse(null);
            assertThat(resultEvent).isNull();
            //유저가 정지되었는지 확인
            UserSuspensionStatusResponse suspensionStatus = userSuspensionService.getUserSuspensionStatus(writer.getId());
            assertThat(suspensionStatus.isSuspended()).isTrue();
            //report status가 변경되었는지 확인
            EventReportStatus resultReportStatus = eventReportStatusRepository.findById(eventReportStatus.getId()).get();
            assertThat(resultReportStatus.getState()).isEqualTo(ReportStatusState.ACCEPTED);
            assertThat(resultReportStatus.isReportedContentDeleted()).isTrue();
            assertThat(resultReportStatus.isReportedContentWriterSuspended()).isTrue();
        }

        @Test
        @DisplayName("둘 다 false이면 아무일도 일어나지 않고 상태가 CANCELED가 된다")
        void deleteTargetContent_and_suspendUser_false() {
            //given
            CompleteReportStatusRequest completeReportStatusRequest = CompleteReportStatusRequest.builder().build();

            //when
            reportStatusService.completeReportStatus(adminInfo, eventReportStatus.getId(), completeReportStatusRequest);

            //then
            em.flush();
            em.clear();
            //event가 soft delete 되지 않았는지 확인
            Event resultEvent = eventRepository.findByIdIfNotDeleted(event.getId()).orElse(null);
            assertThat(resultEvent).isNotNull();
            //유저가 정지되지 않았는지 확인
            UserSuspensionStatusResponse suspensionStatus = userSuspensionService.getUserSuspensionStatus(writer.getId());
            assertThat(suspensionStatus.isSuspended()).isFalse();
            //report status가 변경되었는지 확인
            EventReportStatus resultReportStatus = eventReportStatusRepository.findById(eventReportStatus.getId()).get();
            assertThat(resultReportStatus.getState()).isEqualTo(ReportStatusState.CANCELED);
            assertThat(resultReportStatus.isReportedContentDeleted()).isFalse();
            assertThat(resultReportStatus.isReportedContentWriterSuspended()).isFalse();
        }
    }

    @Nested
    @DisplayName("모집글 신고 상태 완료 테스트")
    class CompleteRecruitmentReportStatusTest {
        User admin;
        User writer;
        Recruitment recruitment;
        RecruitmentReportStatus recruitmentReportStatus;
        AuthenticatedUserInfo adminInfo;

        @BeforeEach
        void init() {
            admin = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());
            writer = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            adminInfo = new AuthenticatedUserInfo(admin.getId(), List.of(GeneralRoleType.ADMIN.name()));

            AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(writer.getId());
            Long eventId = eventService.registerEvent(writerInfo, eventRegisterRequest);

            RecruitmentRegisterRequest recruitmentRegisterRequest = RecruitmentSample.getNormalRegisterRequest();
            Long recruitmentId = recruitmentService.registerRecruitment(writerInfo, eventId, recruitmentRegisterRequest);
            recruitment = em.find(Recruitment.class, recruitmentId);

            recruitmentReportStatus = recruitmentReportStatusRepository.save(
                    RecruitmentReportStatus.builder()
                            .recruitment(recruitment)
                            .build()
            );
            em.flush();
            em.clear();
        }

        @Test
        @DisplayName("신고 대상 컨텐츠를 삭제하면 soft delete 되고 상태가 ACCEPTED가 된다")
        void deleteTargetContent_true() {
            //given
            CompleteReportStatusRequest completeReportStatusRequest = CompleteReportStatusRequest.builder()
                    .deleteTargetContent(true)
                    .build();

            //when
            reportStatusService.completeReportStatus(adminInfo, recruitmentReportStatus.getId(), completeReportStatusRequest);

            //then
            em.flush();
            em.clear();
            //recruitment가 soft delete 되었는지 확인
            Recruitment resultRecruitment = recruitmentRepository.findByIdIfNotDeleted(recruitment.getId()).orElse(null);
            assertThat(resultRecruitment).isNull();
            //report status가 변경되었는지 확인
            RecruitmentReportStatus resultReportStatus = recruitmentReportStatusRepository.findById(recruitmentReportStatus.getId()).get();
            assertThat(resultReportStatus.getState()).isEqualTo(ReportStatusState.ACCEPTED);
            assertThat(resultReportStatus.isReportedContentDeleted()).isTrue();
        }
    }

    @Nested
    @DisplayName("댓글 신고 상태 완료 테스트")
    class CompletePostCommentReportStatusTest {
        User admin;
        User writer;
        PostComment postComment;
        CommentReportStatus commentReportStatus;
        AuthenticatedUserInfo adminInfo;

        @BeforeEach
        void init() {
            admin = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());
            writer = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            adminInfo = new AuthenticatedUserInfo(admin.getId(), List.of(GeneralRoleType.ADMIN.name()));

            AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(writer.getId());
            Long eventId = eventService.registerEvent(writerInfo, eventRegisterRequest);
            Long postCommentId = eventCommentService.registerComment(writerInfo, eventId, new PostCommentRegisterRequest("test"));
            postComment = em.find(PostComment.class, postCommentId);

            commentReportStatus = commentReportStatusRepository.save(
                    CommentReportStatus.builder()
                            .postComment(postComment)
                            .build()
            );
            em.flush();
            em.clear();
        }

        @Test
        @DisplayName("신고 대상 컨텐츠를 삭제하면 delete 되고 상태가 ACCEPTED가 된다")
        void deleteTargetContent_true() {
            //given
            CompleteReportStatusRequest completeReportStatusRequest = CompleteReportStatusRequest.builder()
                    .deleteTargetContent(true)
                    .build();

            //when
            reportStatusService.completeReportStatus(adminInfo, commentReportStatus.getId(), completeReportStatusRequest);

            //then
            em.flush();
            em.clear();
            //postComment가 null이 되었는지 확인
            PostComment resultPostComment = em.find(PostComment.class, postComment.getId());
            assertThat(resultPostComment).isNull();
            //report status가 변경되었는지 확인
            CommentReportStatus resultReportStatus = commentReportStatusRepository.findById(commentReportStatus.getId()).get();
            assertThat(resultReportStatus.getState()).isEqualTo(ReportStatusState.ACCEPTED);
            assertThat(resultReportStatus.isReportedContentDeleted()).isTrue();
        }
    }

    @Nested
    @DisplayName("대댓글 신고 상태 완료 테스트")
    class CompletePostCommentChildReportStatusTest {
        User admin;
        User writer;
        PostCommentChild postCommentChild;
        CommentChildReportStatus commentChildReportStatus;
        AuthenticatedUserInfo adminInfo;

        @BeforeEach
        void init() {
            admin = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());
            writer = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            adminInfo = new AuthenticatedUserInfo(admin.getId(), List.of(GeneralRoleType.ADMIN.name()));

            AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(writer.getId());
            Long eventId = eventService.registerEvent(writerInfo, eventRegisterRequest);

            Long postCommentId = eventCommentService.registerComment(writerInfo, eventId, new PostCommentRegisterRequest("test"));
            Long postCommentChildId = eventCommentService.registerCommentChild(writerInfo, postCommentId, new PostCommentChildRegisterRequest("test", null));
            postCommentChild = em.find(PostCommentChild.class, postCommentChildId);

            commentChildReportStatus = commentChildReportStatusRepository.save(
                    CommentChildReportStatus.builder()
                            .postCommentChild(postCommentChild)
                            .build()
            );
            em.flush();
            em.clear();
        }

        @Test
        @DisplayName("신고 대상 컨텐츠를 삭제하면 delete 되고 상태가 ACCEPTED가 된다")
        void deleteTargetContent_true() {
            //given
            CompleteReportStatusRequest completeReportStatusRequest = CompleteReportStatusRequest.builder()
                    .deleteTargetContent(true)
                    .build();

            //when
            reportStatusService.completeReportStatus(adminInfo, commentChildReportStatus.getId(), completeReportStatusRequest);

            //then
            em.flush();
            em.clear();

        }
    }
}