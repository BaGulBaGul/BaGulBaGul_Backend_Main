package com.BaGulBaGul.BaGulBaGul.domain.report.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.exception.EventNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostComment;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentChild;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.PostCommentChildNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.PostCommentNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentChildRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.exception.RecruitmentNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.RecruitmentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.report.CommentChildReport;
import com.BaGulBaGul.BaGulBaGul.domain.report.CommentChildReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.CommentReport;
import com.BaGulBaGul.BaGulBaGul.domain.report.CommentReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.EventReport;
import com.BaGulBaGul.BaGulBaGul.domain.report.EventReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.RecruitmentReport;
import com.BaGulBaGul.BaGulBaGul.domain.report.RecruitmentReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.Report;
import com.BaGulBaGul.BaGulBaGul.domain.report.Report.ReportBuilder;
import com.BaGulBaGul.BaGulBaGul.domain.report.ReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportType;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.request.ReportRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.response.ReportInfo;
import com.BaGulBaGul.BaGulBaGul.domain.report.exception.DuplicateReportException;
import com.BaGulBaGul.BaGulBaGul.domain.report.exception.ReportStatusNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.report.repository.CommentChildReportStatusRepository;
import com.BaGulBaGul.BaGulBaGul.domain.report.repository.CommentReportStatusRepository;
import com.BaGulBaGul.BaGulBaGul.domain.report.repository.EventReportStatusRepository;
import com.BaGulBaGul.BaGulBaGul.domain.report.repository.RecruitmentReportStatusRepository;
import com.BaGulBaGul.BaGulBaGul.domain.report.repository.ReportRepository;
import com.BaGulBaGul.BaGulBaGul.domain.report.repository.ReportStatusRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.UserNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final EventRepository eventRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostCommentChildRepository postCommentChildRepository;
    private final ReportRepository reportRepository;
    private final ReportStatusRepository reportStatusRepository;
    private final EventReportStatusRepository eventReportStatusRepository;
    private final RecruitmentReportStatusRepository recruitmentReportStatusRepository;
    private final CommentReportStatusRepository commentReportStatusRepository;
    private final CommentChildReportStatusRepository commentChildReportStatusRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Page<ReportInfo> findByReportStatusIdAndPageable(Long reportStatusId, Pageable pageable) {
        ReportStatus reportStatus = reportStatusRepository.findById(reportStatusId)
                .orElseThrow(ReportStatusNotExistException::new);
        Page<Report> reportPage = reportRepository.findByReportStatus(reportStatus, pageable);
        return reportPage.map(ReportInfo::from);
    }

    @Override
    @Transactional(noRollbackFor = DataIntegrityViolationException.class)
    public Long registerEventReport(Long eventId, ReportRegisterRequest reportRegisterRequest) throws DuplicateReportException {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(EventNotFoundException::new);
        Post post = event.getPost();
        User postWriter = post.getUser();

        //EventReport를 저장한다
        EventReport report = saveReport(
                EventReport.builder()
                        .event(event),
                postWriter,
                reportRegisterRequest
        );

        //EventReportStatus에 새로운 EventReport를 반영한다
        handleReportStatus(
                () -> eventReportStatusRepository
                        .findByActiveEventId(eventId)
                        .orElse(null),
                () -> EventReportStatus.builder()
                        .event(event)
                        .build(),
                report
        );
        return report.getReportId();
    }

    @Override
    @Transactional(noRollbackFor = DataIntegrityViolationException.class)
    public Long registerRecruitmentReport(Long recruitmentId, ReportRegisterRequest reportRegisterRequest) throws DuplicateReportException {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(RecruitmentNotFoundException::new);
        Post post = recruitment.getPost();
        User postWriter = post.getUser();

        RecruitmentReport report = saveReport(
                RecruitmentReport.builder()
                        .recruitment(recruitment),
                postWriter,
                reportRegisterRequest
        );
        //RecruitmentReportStatus에 새로운 RecruitmentReport를 반영한다
        handleReportStatus(
                () -> recruitmentReportStatusRepository
                        .findByActiveRecruitmentId(recruitmentId)
                        .orElse(null),
                () -> RecruitmentReportStatus.builder()
                        .recruitment(recruitment)
                        .build(),
                report
        );
        return report.getReportId();
    }

    @Override
    @Transactional
    public Long registerPostCommentReport(Long postCommentId, ReportRegisterRequest reportRegisterRequest) throws DuplicateReportException {
        PostComment postComment = postCommentRepository.findByIdIfNotDeleted(postCommentId)
                .orElseThrow(PostCommentNotFoundException::new);
        User postCommentWriter = postComment.getUser();

        CommentReport report = saveReport(
                CommentReport.builder()
                        .postComment(postComment),
                postCommentWriter,
                reportRegisterRequest
        );
        //CommentReportStatus에 새로운 CommentReport를 반영한다
        handleReportStatus(
                () -> commentReportStatusRepository
                        .findByActivePostCommentId(postCommentId)
                        .orElse(null),
                () -> CommentReportStatus.builder()
                        .postComment(postComment)
                        .build(),
                report
        );
        return report.getReportId();
    }

    @Override
    @Transactional
    public Long registerPostCommentChildReport(Long postCommentChildId, ReportRegisterRequest reportRegisterRequest) throws DuplicateReportException {
        PostCommentChild postCommentChild = postCommentChildRepository.findByIdIfNotDeleted(postCommentChildId).orElseThrow(
                PostCommentChildNotFoundException::new);
        User postCommentChildWriter = postCommentChild.getUser();

        CommentChildReport report = saveReport(
                CommentChildReport.builder()
                        .postCommentChild(postCommentChild),
                postCommentChildWriter,
                reportRegisterRequest
        );
        //CommentChildeportStatus에 새로운 CommentChildReport를 반영한다
        handleReportStatus(
                () -> commentChildReportStatusRepository
                        .findByActivePostCommentChildId(postCommentChildId)
                        .orElse(null),
                () -> CommentChildReportStatus.builder()
                        .postCommentChild(postCommentChild)
                        .build(),
                report
        );
        return report.getReportId();
    }

    /**
     *
     * @param reportBuilder Report 빌더(하위 타입의 빌더에 필요한 값을 미리 채워서 전달한다)
     * @param reportedUser 신고당한 유저
     * @param reportRegisterRequest Report 생성 요청
     * @return 생성하고 저장한 Report의 하위 타입
     * @param <C> 생성할 Report의 하위 타입
     * @param <B> 생성할 Report의 하위 타입의 빌더
     * @throws DuplicateReportException 한 유저가 같은 게시물을 중복 신고하는 것은 불가능하다
     */
    private <C extends Report, B extends ReportBuilder<C, B>> C saveReport(
            ReportBuilder<C, B> reportBuilder, User reportedUser, ReportRegisterRequest reportRegisterRequest
    ) throws DuplicateReportException {
        ReportType reportType = reportRegisterRequest.getReportType();
        User reportingUser = userRepository.findById(reportRegisterRequest.getReportingUserId())
                .orElseThrow(UserNotFoundException::new);
        String message = reportRegisterRequest.getMessage();

        Report report = createNewReport(
                reportBuilder,
                reportType,
                reportedUser,
                reportingUser,
                message
        );
        try {
            report = reportRepository.save(report);
        } catch (DataIntegrityViolationException e) {
            if(e.getMessage().contains("UK__REPORT__reporting_user__")) {
                throw new DuplicateReportException();
            }
        }
        return (C) report;
    }

    /**
     * 새로운 Report를 생성한다
     * @param reportBuilder 신고 빌더
     * @param reportType 신고 타입
     * @param reportedUser 신고 당한 유저
     * @param reportingUser 신고한 유저
     * @param message 신고 메세지
     * @return 생성한 Report
     */
    private Report createNewReport (ReportBuilder reportBuilder, ReportType reportType, User reportedUser, User reportingUser, String message) {
        return reportBuilder
                .reportType(reportType)
                .reportedUser(reportedUser)
                .reportingUser(reportingUser)
                .message(message)
                .build();
    }

    /**
     * 신고 대상과 연결된 ReportStatus에 Report정보를 반영한다.
     * @param reportStatusFinder ReportStatus를 찾는 메서드
     * @param reportStatusCreator ReportStatus를 생성하는 메서드
     * @param report ReportStatus에 반영할 Report
     */
    private void handleReportStatus(
            Supplier<ReportStatus> reportStatusFinder,
            Supplier<ReportStatus> reportStatusCreator,
            Report report
    ) {
        // 활성화 되어 있는 EventReportStatus를 찾는다.
        ReportStatus reportStatus = reportStatusFinder.get();
        // 활성화 되어 있는 것이 없다면
        if(reportStatus == null) {
            // 새로운 ReportStatus를 생성
            reportStatus = reportStatusCreator.get();
            try {
                // 새로운 ReportStatus를 저장.
                // ReportStatus는 어떤 target(Event, Recruitment, PostComment, PostCommentChild)을 가진 행에 대해
                // state가 PROCEEDING이라면 active_target_id를 target_id로 설정하고
                // unique조건을 부여해서 활성화된 행이 1개임을 보장한다.
                reportStatus = reportStatusRepository.save(reportStatus);
            } catch (DataIntegrityViolationException e) {
                // 만약 유니크 예외라면 동시성 문제로 첫번째 탐색과 저장 사이에 다른 트랜젝션이 먼저 저장한 것이므로 다시 찾는다.
                if(e.getMessage().contains("UK__REPORT_STATUS__ACTIVE_")) {
                    // 다시 찾는다
                    reportStatus = reportStatusFinder.get();
                    // 다시 찾아도 없다면 예외. 사실상 불가능한 경우이다.
                    // 다른 트랜잭션에서 ReportStatus를 동시에 생성해서 unique 예외가 났고 다시 탐색하는 사이에 관리자가 이를 처리했다면 가능하다.
                    if(reportStatus == null) {
                        throw new GeneralException(ResponseCode.INTERNAL_SERVER_ERROR);
                    }
                }
            }
        }
        //Report에 ReportStatus를 할당한다.
        report.setReportStatus(reportStatus);
        //ReportStatus의 통계 필드에 현재 Report정보를 반영한다
        if(report.getReportType() == ReportType.NOT_RELEVANT) {
            reportStatusRepository.increaseNotRelevantReportCount(reportStatus.getId());
        } else if(report.getReportType() == ReportType.OFFENSIVE_CONTENT) {
            reportStatusRepository.increaseOffensiveContentReportCount(reportStatus.getId());
        } else if(report.getReportType() == ReportType.DEFAMATORY) {
            reportStatusRepository.increaseDefamatoryReportCount(reportStatus.getId());
        } else if(report.getReportType() == ReportType.ETC) {
            reportStatusRepository.increaseEctReportCount(reportStatus.getId());
        }
    }
}
