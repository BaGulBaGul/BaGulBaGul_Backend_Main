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
import com.BaGulBaGul.BaGulBaGul.domain.report.CommentReport;
import com.BaGulBaGul.BaGulBaGul.domain.report.EventReport;
import com.BaGulBaGul.BaGulBaGul.domain.report.RecruitmentReport;
import com.BaGulBaGul.BaGulBaGul.domain.report.Report;
import com.BaGulBaGul.BaGulBaGul.domain.report.Report.ReportBuilder;
import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportType;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.ReportRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.report.exception.DuplicateReportException;
import com.BaGulBaGul.BaGulBaGul.domain.report.repository.ReportRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.UserNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Long registerEventReport(Long eventId, ReportRegisterRequest reportRegisterRequest) throws DuplicateReportException {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(EventNotFoundException::new);
        Post post = event.getPost();
        User postWriter = post.getUser();

        return saveReport(
                EventReport.builder()
                        .event(event),
                postWriter,
                reportRegisterRequest
        );
    }

    @Override
    @Transactional
    public Long registerRecruitmentReport(Long recruitmentId, ReportRegisterRequest reportRegisterRequest) throws DuplicateReportException {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(RecruitmentNotFoundException::new);
        Post post = recruitment.getPost();
        User postWriter = post.getUser();

        return saveReport(
                RecruitmentReport.builder()
                        .recruitment(recruitment),
                postWriter,
                reportRegisterRequest
        );
    }

    @Override
    @Transactional
    public Long registerPostCommentReport(Long postCommentId, ReportRegisterRequest reportRegisterRequest) throws DuplicateReportException {
        PostComment postComment = postCommentRepository.findById(postCommentId)
                .orElseThrow(PostCommentNotFoundException::new);
        User postCommentWriter = postComment.getUser();

        return saveReport(
                CommentReport.builder()
                        .postComment(postComment),
                postCommentWriter,
                reportRegisterRequest
        );
    }

    @Override
    @Transactional
    public Long registerPostCommentChildReport(Long postCommentChildId, ReportRegisterRequest reportRegisterRequest) throws DuplicateReportException {
        PostCommentChild postCommentChild = postCommentChildRepository.findById(postCommentChildId).orElseThrow(
                PostCommentChildNotFoundException::new);
        User postCommentChildWriter = postCommentChild.getUser();

        return saveReport(
                CommentChildReport.builder()
                        .postCommentChild(postCommentChild),
                postCommentChildWriter,
                reportRegisterRequest
        );
    }

    private Long saveReport(ReportBuilder reportBuilder, User reportedUser, ReportRegisterRequest reportRegisterRequest) throws DuplicateReportException {
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
            reportRepository.save(report);
        } catch (DataIntegrityViolationException e) {
            if(e.getMessage().contains("UK__REPORT")) {
                throw new DuplicateReportException();
            }
        }
        return report.getReportId();
    }

    private Report createNewReport (ReportBuilder reportBuilder, ReportType reportType, User reportedUser, User reportingUser, String message) {
        return reportBuilder
                .reportType(reportType)
                .reportedUser(reportedUser)
                .reportingUser(reportingUser)
                .message(message)
                .build();
    }
}
