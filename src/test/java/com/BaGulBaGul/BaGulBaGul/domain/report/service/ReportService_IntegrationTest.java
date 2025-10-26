
package com.BaGulBaGul.BaGulBaGul.domain.report.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.sampledata.EventSample;
import com.BaGulBaGul.BaGulBaGul.domain.event.service.EventCommentService;
import com.BaGulBaGul.BaGulBaGul.domain.event.service.EventService;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentChildRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request.RecruitmentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.sampledata.RecruitmentSample;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.service.RecruitmentCommentService;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.service.RecruitmentService;
import com.BaGulBaGul.BaGulBaGul.domain.report.CommentChildReport;
import com.BaGulBaGul.BaGulBaGul.domain.report.CommentChildReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.CommentReport;
import com.BaGulBaGul.BaGulBaGul.domain.report.CommentReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.EventReport;
import com.BaGulBaGul.BaGulBaGul.domain.report.EventReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.RecruitmentReport;
import com.BaGulBaGul.BaGulBaGul.domain.report.RecruitmentReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.Report;
import com.BaGulBaGul.BaGulBaGul.domain.report.ReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportContentType;
import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportStatusState;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.request.ReportRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.response.ReportStatusInfo;
import com.BaGulBaGul.BaGulBaGul.domain.report.exception.DuplicateReportException;
import com.BaGulBaGul.BaGulBaGul.domain.report.repository.CommentChildReportStatusRepository;
import com.BaGulBaGul.BaGulBaGul.domain.report.repository.CommentReportStatusRepository;
import com.BaGulBaGul.BaGulBaGul.domain.report.repository.EventReportStatusRepository;
import com.BaGulBaGul.BaGulBaGul.domain.report.repository.RecruitmentReportStatusRepository;
import com.BaGulBaGul.BaGulBaGul.domain.report.repository.ReportRepository;
import com.BaGulBaGul.BaGulBaGul.domain.report.sampledata.ReportSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.GeneralRoleType;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import com.BaGulBaGul.BaGulBaGul.domain.report.ReportTestUtils;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.response.ReportInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("신고 서비스 통합 테스트")
class ReportService_IntegrationTest {

    @Autowired
    ReportService reportService;
    @Autowired
    ReportRepository reportRepository;
    @Autowired
    UserJoinService userJoinService;
    @Autowired
    EventService eventService;
    @Autowired
    RecruitmentService recruitmentService;
    @Autowired
    EventCommentService eventCommentService;
    @Autowired
    RecruitmentCommentService recruitmentCommentService;
    @Autowired
    EntityManager em;
    @Autowired
    EventReportStatusRepository eventReportStatusRepository;
    @Autowired
    RecruitmentReportStatusRepository recruitmentReportStatusRepository;
    @Autowired
    CommentReportStatusRepository commentReportStatusRepository;
    @Autowired
    CommentChildReportStatusRepository commentChildReportStatusRepository;

    @Nested
    @DisplayName("이벤트 신고")
    class RegisterEventReportTest {

        User reportingUser;
        User reportingUser2;
        User reportedUser;
        Long eventId;

        @BeforeEach
        void init() {
            reportingUser = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            reportingUser2 = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            reportedUser = userJoinService.registerUser(UserSample.getNormal3UserRegisterRequest());
            AuthenticatedUserInfo reportedUserInfo = new AuthenticatedUserInfo(reportedUser.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));

            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(reportedUser.getId());
            eventId = eventService.registerEvent(reportedUserInfo, eventRegisterRequest);
        }

        @Test
        @DisplayName("성공 테스트")
        void registerEventReport_success() {
            //given
            ReportRegisterRequest reportRegisterRequest = ReportSample.getNormalReportRegisterRequest(reportingUser.getId());

            //when
            Long reportId = reportService.registerEventReport(eventId, reportRegisterRequest);

            //then
            em.flush();
            em.clear();
            Report report = reportRepository.findById(reportId).orElse(null);
            assertThat(report).isNotNull();
            assertThat(report).isInstanceOf(EventReport.class);
            // EventReport 확인
            EventReport eventReport = (EventReport) report;
            assertThat(eventReport.getEvent().getId()).isEqualTo(eventId);
            // Report 확인
            assertThat(report.getReportingUser().getId()).isEqualTo(reportingUser.getId());
            assertThat(report.getReportedUser().getId()).isEqualTo(reportedUser.getId());
            assertThat(report.getMessage()).isEqualTo(reportRegisterRequest.getMessage());
            assertThat(report.getReportType()).isEqualTo(reportRegisterRequest.getReportType());
            // EventReportStatus 확인
            EventReportStatus eventReportStatus = eventReportStatusRepository.findByActiveEventId(eventId).orElse(null);
            assertThat(eventReportStatus).isNotNull();
            assertThat(eventReportStatus.getEvent().getId()).isEqualTo(eventId);
            assertThat(eventReportStatus.getActiveEventId()).isEqualTo(eventId);
            // ReportStatus 확인
            ReportTestUtils.assertReportStatus(
                    eventReportStatus, ReportStatusState.PROCEEDING,
                    1, 0, 1, 0, 0,
                    false, false
            );
        }

        @Test
        @DisplayName("중복 신고라면 DuplicateReportException 예외가 발생해야 한다.")
        void registerEventReport_duplicate() {
            //given
            ReportRegisterRequest reportRegisterRequest = ReportSample.getNormalReportRegisterRequest(reportingUser.getId());
            reportService.registerEventReport(eventId, reportRegisterRequest);
            em.flush();
            em.clear();

            //when
            //then
            assertThrows(
                    DuplicateReportException.class,
                    () -> reportService.registerEventReport(eventId, reportRegisterRequest)
            );
        }

        @Test
        @DisplayName("활성화된 ReportStatus가 있다면 통합되어 반영되어야 한다")
        void registerEventReport_twice() {
            //given
            ReportRegisterRequest reportRegisterRequest = ReportSample.getNormalReportRegisterRequest(reportingUser.getId());
            reportService.registerEventReport(eventId, reportRegisterRequest);
            em.flush();
            em.clear();

            //when
            ReportRegisterRequest reportRegisterRequest2 = ReportSample.getNormal2ReportRegisterRequest(reportingUser2.getId());
            Long reportId2 = reportService.registerEventReport(eventId, reportRegisterRequest2);

            //then
            em.flush();
            em.clear();
            Report report = reportRepository.findById(reportId2).orElse(null);
            assertThat(report).isNotNull();
            assertThat(report).isInstanceOf(EventReport.class);
            // EventReport 확인
            EventReport eventReport = (EventReport) report;
            assertThat(eventReport.getEvent().getId()).isEqualTo(eventId);
            // Report 확인
            assertThat(report.getReportingUser().getId()).isEqualTo(reportingUser2.getId());
            assertThat(report.getReportedUser().getId()).isEqualTo(reportedUser.getId());
            assertThat(report.getMessage()).isEqualTo(reportRegisterRequest2.getMessage());
            assertThat(report.getReportType()).isEqualTo(reportRegisterRequest2.getReportType());
            // EventReportStatus 확인
            EventReportStatus eventReportStatus = eventReportStatusRepository.findByActiveEventId(eventId).orElse(null);
            assertThat(eventReportStatus).isNotNull();
            assertThat(eventReportStatus.getEvent().getId()).isEqualTo(eventId);
            assertThat(eventReportStatus.getActiveEventId()).isEqualTo(eventId);
            // ReportStatus 확인
            ReportTestUtils.assertReportStatus(
                    eventReportStatus, ReportStatusState.PROCEEDING,
                    2, 1, 1, 0, 0,
                    false, false
            );
        }
    }

    @Nested
    @DisplayName("모집글 신고")
    class RegisterRecruitmentReportTest {

        User reportingUser;
        User reportingUser2;
        User reportedUser;
        Long eventId;
        Long recruitmentId;

        @BeforeEach
        void init() {
            reportingUser = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            reportingUser2 = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            reportedUser = userJoinService.registerUser(UserSample.getNormal3UserRegisterRequest());
            AuthenticatedUserInfo reportedUserInfo = new AuthenticatedUserInfo(reportedUser.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));

            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(reportedUser.getId());
            eventId = eventService.registerEvent(reportedUserInfo, eventRegisterRequest);
            RecruitmentRegisterRequest recruitmentRegisterRequest = RecruitmentSample.getNormalRegisterRequest();
            recruitmentId = recruitmentService.registerRecruitment(reportedUserInfo, eventId, recruitmentRegisterRequest);
        }

        @Test
        @DisplayName("성공 테스트")
        void registerRecruitmentReport_success() {
            //given
            ReportRegisterRequest reportRegisterRequest = ReportSample.getNormalReportRegisterRequest(reportingUser.getId());

            //when
            Long reportId = reportService.registerRecruitmentReport(recruitmentId, reportRegisterRequest);

            //then
            em.flush();
            em.clear();
            Report report = reportRepository.findById(reportId).orElse(null);
            assertThat(report).isNotNull();
            assertThat(report).isInstanceOf(RecruitmentReport.class);
            RecruitmentReport recruitmentReport = (RecruitmentReport) report;
            assertThat(recruitmentReport.getRecruitment().getId()).isEqualTo(recruitmentId);
            assertThat(report.getReportingUser().getId()).isEqualTo(reportingUser.getId());
            assertThat(report.getReportedUser().getId()).isEqualTo(reportedUser.getId());
            assertThat(report.getMessage()).isEqualTo(reportRegisterRequest.getMessage());
            assertThat(report.getReportType()).isEqualTo(reportRegisterRequest.getReportType());
            // RecruitmentReportStatus 확인
            RecruitmentReportStatus recruitmentReportStatus = recruitmentReportStatusRepository.findByActiveRecruitmentId(recruitmentId).orElse(null);
            assertThat(recruitmentReportStatus).isNotNull();
            assertThat(recruitmentReportStatus.getRecruitment().getId()).isEqualTo(recruitmentId);
            assertThat(recruitmentReportStatus.getActiveRecruitmentId()).isEqualTo(recruitmentId);
            // ReportStatus 확인
            ReportTestUtils.assertReportStatus(
                    recruitmentReportStatus, ReportStatusState.PROCEEDING,
                    1, 0, 1, 0, 0,
                    false, false
            );
        }

        @Test
        @DisplayName("중복 신고라면 DuplicateReportException 예외가 발생해야 한다.")
        void registerRecruitmentReport_duplicate() {
            //given
            ReportRegisterRequest reportRegisterRequest = ReportSample.getNormalReportRegisterRequest(reportingUser.getId());
            reportService.registerRecruitmentReport(recruitmentId, reportRegisterRequest);
            em.flush();
            em.clear();

            //when
            //then
            assertThrows(
                    DuplicateReportException.class,
                    () -> reportService.registerRecruitmentReport(recruitmentId, reportRegisterRequest)
            );
        }

        @Test
        @DisplayName("활성화된 ReportStatus가 있다면 통합되어 반영되어야 한다")
        void registerRecruitmentReport_twice() {
            //given
            ReportRegisterRequest reportRegisterRequest = ReportSample.getNormalReportRegisterRequest(reportingUser.getId());
            reportService.registerRecruitmentReport(recruitmentId, reportRegisterRequest);
            em.flush();
            em.clear();

            //when
            ReportRegisterRequest reportRegisterRequest2 = ReportSample.getNormal2ReportRegisterRequest(reportingUser2.getId());
            Long reportId2 = reportService.registerRecruitmentReport(recruitmentId, reportRegisterRequest2);

            //then
            em.flush();
            em.clear();
            Report report = reportRepository.findById(reportId2).orElse(null);
            assertThat(report).isNotNull();
            assertThat(report).isInstanceOf(RecruitmentReport.class);
            // RecruitmentReport 확인
            RecruitmentReport recruitmentReport = (RecruitmentReport) report;
            assertThat(recruitmentReport.getRecruitment().getId()).isEqualTo(recruitmentId);
            // Report 확인
            assertThat(report.getReportingUser().getId()).isEqualTo(reportingUser2.getId());
            assertThat(report.getReportedUser().getId()).isEqualTo(reportedUser.getId());
            assertThat(report.getMessage()).isEqualTo(reportRegisterRequest2.getMessage());
            assertThat(report.getReportType()).isEqualTo(reportRegisterRequest2.getReportType());
            // RecruitmentReportStatus 확인
            RecruitmentReportStatus recruitmentReportStatus = recruitmentReportStatusRepository.findByActiveRecruitmentId(recruitmentId).orElse(null);
            assertThat(recruitmentReportStatus).isNotNull();
            assertThat(recruitmentReportStatus.getRecruitment().getId()).isEqualTo(recruitmentId);
            assertThat(recruitmentReportStatus.getActiveRecruitmentId()).isEqualTo(recruitmentId);
            // ReportStatus 확인
            ReportTestUtils.assertReportStatus(
                    recruitmentReportStatus, ReportStatusState.PROCEEDING,
                    2, 1, 1, 0, 0,
                    false, false
            );
        }
    }

    @Nested
    @DisplayName("댓글 신고")
    class RegisterPostCommentReportTest {

        User reportingUser;
        User reportingUser2;
        User reportedUser;
        Long eventId;
        Long postCommentId;

        @BeforeEach
        void init() {
            reportingUser = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            reportingUser2 = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            reportedUser = userJoinService.registerUser(UserSample.getNormal3UserRegisterRequest());
            AuthenticatedUserInfo reportedUserInfo = new AuthenticatedUserInfo(reportedUser.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));

            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(reportedUser.getId());
            eventId = eventService.registerEvent(reportedUserInfo, eventRegisterRequest);
            PostCommentRegisterRequest postCommentRegisterRequest = new PostCommentRegisterRequest("test comment");
            postCommentId = eventCommentService.registerComment(reportedUserInfo, eventId, postCommentRegisterRequest);
        }

        @Test
        @DisplayName("성공 테스트")
        void registerPostCommentReport_success() {
            //given
            ReportRegisterRequest reportRegisterRequest = ReportSample.getNormalReportRegisterRequest(reportingUser.getId());

            //when
            Long reportId = reportService.registerPostCommentReport(postCommentId, reportRegisterRequest);

            //then
            em.flush();
            em.clear();
            Report report = reportRepository.findById(reportId).orElse(null);
            assertThat(report).isNotNull();
            assertThat(report).isInstanceOf(CommentReport.class);
            CommentReport commentReport = (CommentReport) report;
            assertThat(commentReport.getPostComment().getId()).isEqualTo(postCommentId);
            assertThat(report.getReportingUser().getId()).isEqualTo(reportingUser.getId());
            assertThat(report.getReportedUser().getId()).isEqualTo(reportedUser.getId());
            assertThat(report.getMessage()).isEqualTo(reportRegisterRequest.getMessage());
            assertThat(report.getReportType()).isEqualTo(reportRegisterRequest.getReportType());
            // CommentReportStatus 확인
            CommentReportStatus commentReportStatus = commentReportStatusRepository.findByActivePostCommentId(postCommentId).orElse(null);
            assertThat(commentReportStatus).isNotNull();
            assertThat(commentReportStatus.getPostComment().getId()).isEqualTo(postCommentId);
            assertThat(commentReportStatus.getActivePostCommentId()).isEqualTo(postCommentId);
            // ReportStatus 확인
            ReportTestUtils.assertReportStatus(
                    commentReportStatus, ReportStatusState.PROCEEDING,
                    1, 0, 1, 0, 0,
                    false, false
            );
        }

        @Test
        @DisplayName("중복 신고라면 DuplicateReportException 예외가 발생해야 한다.")
        void registerPostCommentReport_duplicate() {
            //given
            ReportRegisterRequest reportRegisterRequest = ReportSample.getNormalReportRegisterRequest(reportingUser.getId());
            reportService.registerPostCommentReport(postCommentId, reportRegisterRequest);
            em.flush();
            em.clear();

            //when
            //then
            assertThrows(
                    DuplicateReportException.class,
                    () -> reportService.registerPostCommentReport(postCommentId, reportRegisterRequest)
            );
        }

        @Test
        @DisplayName("활성화된 ReportStatus가 있다면 통합되어 반영되어야 한다")
        void registerPostCommentReport_twice() {
            //given
            ReportRegisterRequest reportRegisterRequest = ReportSample.getNormalReportRegisterRequest(reportingUser.getId());
            reportService.registerPostCommentReport(postCommentId, reportRegisterRequest);
            em.flush();
            em.clear();

            //when
            ReportRegisterRequest reportRegisterRequest2 = ReportSample.getNormal2ReportRegisterRequest(reportingUser2.getId());
            Long reportId2 = reportService.registerPostCommentReport(postCommentId, reportRegisterRequest2);

            //then
            em.flush();
            em.clear();
            Report report = reportRepository.findById(reportId2).orElse(null);
            assertThat(report).isNotNull();
            assertThat(report).isInstanceOf(CommentReport.class);
            // CommentReport 확인
            CommentReport commentReport = (CommentReport) report;
            assertThat(commentReport.getPostComment().getId()).isEqualTo(postCommentId);
            // Report 확인
            assertThat(report.getReportingUser().getId()).isEqualTo(reportingUser2.getId());
            assertThat(report.getReportedUser().getId()).isEqualTo(reportedUser.getId());
            assertThat(report.getMessage()).isEqualTo(reportRegisterRequest2.getMessage());
            assertThat(report.getReportType()).isEqualTo(reportRegisterRequest2.getReportType());
            // CommentReportStatus 확인
            CommentReportStatus commentReportStatus = commentReportStatusRepository.findByActivePostCommentId(postCommentId).orElse(null);
            assertThat(commentReportStatus).isNotNull();
            assertThat(commentReportStatus.getPostComment().getId()).isEqualTo(postCommentId);
            assertThat(commentReportStatus.getActivePostCommentId()).isEqualTo(postCommentId);
            // ReportStatus 확인
            ReportTestUtils.assertReportStatus(
                    commentReportStatus, ReportStatusState.PROCEEDING,
                    2, 1, 1, 0, 0,
                    false, false
            );
        }
    }

    @Nested
    @DisplayName("대댓글 신고")
    class RegisterPostCommentChildReportTest {

        private User reportingUser;
        private User reportingUser2;
        private User reportedUser;
        private Long eventId;
        private Long postCommentId;
        private Long postCommentChildId;

        @BeforeEach
        void init() {
            //given
            reportingUser = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            reportingUser2 = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            reportedUser = userJoinService.registerUser(UserSample.getNormal3UserRegisterRequest());
            AuthenticatedUserInfo reportedUserInfo = new AuthenticatedUserInfo(reportedUser.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));

            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(reportedUser.getId());
            eventId = eventService.registerEvent(reportedUserInfo, eventRegisterRequest);
            PostCommentRegisterRequest postCommentRegisterRequest = new PostCommentRegisterRequest("test comment");
            postCommentId = eventCommentService.registerComment(reportedUserInfo, eventId, postCommentRegisterRequest);
            PostCommentChildRegisterRequest postCommentChildRegisterRequest = new PostCommentChildRegisterRequest("test child comment", null);
            postCommentChildId = eventCommentService.registerCommentChild(reportedUserInfo, postCommentId, postCommentChildRegisterRequest);
        }

        @Test
        @DisplayName("성공 테스트")
        void registerPostCommentChildReport_success() {
            //given
            ReportRegisterRequest reportRegisterRequest = ReportSample.getNormalReportRegisterRequest(reportingUser.getId());

            //when
            Long reportId = reportService.registerPostCommentChildReport(postCommentChildId, reportRegisterRequest);

            //then
            em.flush();
            em.clear();
            Report report = reportRepository.findById(reportId).orElse(null);
            assertThat(report).isNotNull();
            assertThat(report).isInstanceOf(CommentChildReport.class);
            CommentChildReport commentChildReport = (CommentChildReport) report;
            assertThat(commentChildReport.getPostCommentChild().getId()).isEqualTo(postCommentChildId);
            assertThat(report.getReportingUser().getId()).isEqualTo(reportingUser.getId());
            assertThat(report.getReportedUser().getId()).isEqualTo(reportedUser.getId());
            assertThat(report.getMessage()).isEqualTo(reportRegisterRequest.getMessage());
            assertThat(report.getReportType()).isEqualTo(reportRegisterRequest.getReportType());
            // CommentChildReportStatus 확인
            CommentChildReportStatus commentChildReportStatus = commentChildReportStatusRepository.findByActivePostCommentChildId(postCommentChildId).orElse(null);
            assertThat(commentChildReportStatus).isNotNull();
            assertThat(commentChildReportStatus.getPostCommentChild().getId()).isEqualTo(postCommentChildId);
            assertThat(commentChildReportStatus.getActivePostCommentChildId()).isEqualTo(postCommentChildId);
            // ReportStatus 확인
            ReportTestUtils.assertReportStatus(
                    commentChildReportStatus, ReportStatusState.PROCEEDING,
                    1, 0, 1, 0, 0,
                    false, false
            );
        }

        @Test
        @DisplayName("중복 신고라면 DuplicateReportException 예외가 발생해야 한다.")
        void registerPostCommentChildReport_duplicate() {
            //given
            ReportRegisterRequest reportRegisterRequest = ReportSample.getNormalReportRegisterRequest(reportingUser.getId());
            reportService.registerPostCommentChildReport(postCommentChildId, reportRegisterRequest);
            em.flush();
            em.clear();

            //when
            //then
            assertThrows(
                    DuplicateReportException.class,
                    () -> reportService.registerPostCommentChildReport(postCommentChildId, reportRegisterRequest)
            );
        }

        @Test
        @DisplayName("활성화된 ReportStatus가 있다면 통합되어 반영되어야 한다")
        void registerPostCommentChildReport_twice() {
            //given
            ReportRegisterRequest reportRegisterRequest = ReportSample.getNormalReportRegisterRequest(reportingUser.getId());
            reportService.registerPostCommentChildReport(postCommentChildId, reportRegisterRequest);
            em.flush();
            em.clear();

            //when
            ReportRegisterRequest reportRegisterRequest2 = ReportSample.getNormal2ReportRegisterRequest(reportingUser2.getId());
            Long reportId2 = reportService.registerPostCommentChildReport(postCommentChildId, reportRegisterRequest2);

            //then
            em.flush();
            em.clear();
            Report report = reportRepository.findById(reportId2).orElse(null);
            assertThat(report).isNotNull();
            assertThat(report).isInstanceOf(CommentChildReport.class);
            // CommentChildReport 확인
            CommentChildReport commentChildReport = (CommentChildReport) report;
            assertThat(commentChildReport.getPostCommentChild().getId()).isEqualTo(postCommentChildId);
            // Report 확인
            assertThat(report.getReportingUser().getId()).isEqualTo(reportingUser2.getId());
            assertThat(report.getReportedUser().getId()).isEqualTo(reportedUser.getId());
            assertThat(report.getMessage()).isEqualTo(reportRegisterRequest2.getMessage());
            assertThat(report.getReportType()).isEqualTo(reportRegisterRequest2.getReportType());
            // CommentChildReportStatus 확인
            CommentChildReportStatus commentChildReportStatus = commentChildReportStatusRepository.findByActivePostCommentChildId(postCommentChildId).orElse(null);
            assertThat(commentChildReportStatus).isNotNull();
            assertThat(commentChildReportStatus.getPostCommentChild().getId()).isEqualTo(postCommentChildId);
            assertThat(commentChildReportStatus.getActivePostCommentChildId()).isEqualTo(postCommentChildId);
            // ReportStatus 확인
            ReportTestUtils.assertReportStatus(
                    commentChildReportStatus, ReportStatusState.PROCEEDING,
                    2, 1, 1, 0, 0,
                    false, false
            );
        }
    }

    @Nested
    @DisplayName("reportStatusId와 pageable로 Report 정보 페이징 조회")
    class FindByReportStatusIdAndPageableTest {
        User reportingUser;
        User reportingUser2;
        User reportedUser;
        Long eventId;
        ReportRegisterRequest reportRegisterRequest;
        ReportRegisterRequest reportRegisterRequest2;
        Long reportId1;
        Long reportId2;
        ReportStatus reportStatus;

        @BeforeEach
        void init() throws InterruptedException {
            reportingUser = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            reportingUser2 = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            reportedUser = userJoinService.registerUser(UserSample.getNormal3UserRegisterRequest());
            AuthenticatedUserInfo reportedUserInfo = new AuthenticatedUserInfo(reportedUser.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));

            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(reportedUser.getId());
            eventId = eventService.registerEvent(reportedUserInfo, eventRegisterRequest);

            reportRegisterRequest = ReportSample.getNormalReportRegisterRequest(reportingUser.getId());
            reportId1 = reportService.registerEventReport(eventId, reportRegisterRequest);
            Thread.sleep(1);
            reportRegisterRequest2 = ReportSample.getNormal2ReportRegisterRequest(reportingUser2.getId());
            reportId2 = reportService.registerEventReport(eventId, reportRegisterRequest2);

            reportStatus = reportRepository.findById(reportId1).get().getReportStatus();
            em.flush();
            em.clear();
        }

        @Test
        @DisplayName("성공 테스트")
        void findByReportStatusIdAndPageable_success() {
            //given
            Pageable pageable = PageRequest.of(0, 10);

            //when
            Page<ReportInfo> result = reportService.findByReportStatusIdAndPageable(reportStatus.getId(), pageable);

            //then
            Report report1 = reportRepository.findById(reportId1).get();
            Report report2 = reportRepository.findById(reportId2).get();
            assertThat(result.getTotalElements()).isEqualTo(2);
            ReportInfo reportInfo1 = result.getContent().get(0);
            ReportInfo reportInfo2 = result.getContent().get(1);
            ReportTestUtils.assertReportInfo(reportInfo1, report1);
            ReportTestUtils.assertReportInfo(reportInfo2, report2);
        }

        @Test
        @DisplayName("페이지네이션 및 정렬 테스트")
        void findByReportStatusIdAndPageable_pagination_and_sort() {
            //given
            Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "createdAt"));

            //when
            Page<ReportInfo> result = reportService.findByReportStatusIdAndPageable(reportStatus.getId(), pageable);

            //then
            Report report1 = reportRepository.findById(reportId1).get();
            assertThat(result.getTotalElements()).isEqualTo(2);
            assertThat(result.getTotalPages()).isEqualTo(2);
            assertThat(result.getSize()).isEqualTo(1);
            assertThat(result.getContent().size()).isEqualTo(1);
            ReportInfo reportInfo1 = result.getContent().get(0);
            ReportTestUtils.assertReportInfo(reportInfo1, report1);

            //given
            pageable = PageRequest.of(1, 1, Sort.by(Sort.Direction.ASC, "createdAt"));

            //when
            result = reportService.findByReportStatusIdAndPageable(reportStatus.getId(), pageable);

            //then
            Report report2 = reportRepository.findById(reportId2).get();
            assertThat(result.getTotalElements()).isEqualTo(2);
            assertThat(result.getTotalPages()).isEqualTo(2);
            assertThat(result.getSize()).isEqualTo(1);
            assertThat(result.getContent().size()).isEqualTo(1);
            ReportInfo reportInfo2 = result.getContent().get(0);
            ReportTestUtils.assertReportInfo(reportInfo2, report2);
        }
    }
}
