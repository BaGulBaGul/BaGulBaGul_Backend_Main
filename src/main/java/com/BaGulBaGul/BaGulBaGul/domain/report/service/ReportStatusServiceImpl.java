package com.BaGulBaGul.BaGulBaGul.domain.report.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.service.EventService;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostCommentService;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.service.RecruitmentService;
import com.BaGulBaGul.BaGulBaGul.domain.report.CommentChildReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.CommentReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.EventReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.RecruitmentReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.ReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportStatusState;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.request.CompleteReportStatusRequest;
import com.BaGulBaGul.BaGulBaGul.domain.report.exception.ReportStatusNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.report.repository.ReportStatusRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.SuspendUserRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserSuspensionService;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportStatusServiceImpl implements ReportStatusService {

    private final ReportStatusRepository reportStatusRepository;

    private final EventService eventService;
    private final RecruitmentService recruitmentService;
    private final PostCommentService postCommentService;
    private final UserSuspensionService userSuspensionService;

    @Override
    @Transactional
    public void completeReportStatus(
            AuthenticatedUserInfo authenticatedUserInfo,
            Long reportStatusId,
            CompleteReportStatusRequest completeReportStatusRequest
    ) {
        ReportStatus reportStatus = reportStatusRepository.findById(reportStatusId)
                .orElseThrow(ReportStatusNotExistException::new);
        // 최종 상태
        ReportStatusState reportStatusState = ReportStatusState.CANCELED;
        // 신고 대상 게시물 삭제 처리
        if(completeReportStatusRequest.isDeleteTargetContent()) {
            handleDeleteTarget(authenticatedUserInfo, reportStatus);
            reportStatusState = ReportStatusState.ACCEPTED;
        }
        // 신고 대상 게시물 작성 유저 정지
        SuspendUserRequest suspendUserRequest = completeReportStatusRequest.getSuspendUserRequest();
        if(suspendUserRequest != null) {
            handleSuspendUser(authenticatedUserInfo, reportStatus, suspendUserRequest);
            reportStatusState = ReportStatusState.ACCEPTED;
        }
        // ReportStatus의 진행 상태를 변경
        reportStatus = reportStatusRepository.findById(reportStatusId).orElseThrow(ReportStatusNotExistException::new);
        reportStatus.setState(reportStatusState);
    }

    private void handleDeleteTarget(AuthenticatedUserInfo authenticatedUserInfo, ReportStatus reportStatus) {
        // 신고 대상 게시물 삭제
        if(reportStatus instanceof EventReportStatus) {
            eventService.deleteEvent(authenticatedUserInfo, ((EventReportStatus) reportStatus).getActiveEventId());
        } else if(reportStatus instanceof RecruitmentReportStatus) {
            recruitmentService.deleteRecruitment(authenticatedUserInfo,
                    ((RecruitmentReportStatus) reportStatus).getActiveRecruitmentId());
        } else if(reportStatus instanceof CommentReportStatus) {
            postCommentService.deletePostComment(((CommentReportStatus) reportStatus).getActivePostCommentId(),
                    authenticatedUserInfo.getUserId());
        } else if(reportStatus instanceof CommentChildReportStatus) {
            postCommentService.deletePostCommentChild(
                    ((CommentChildReportStatus) reportStatus).getActivePostCommentChildId(),
                    authenticatedUserInfo.getUserId()
            );
        }
        // 플래그 설정
        reportStatus.setReportedContentDeleted(true);
        // 중간에 jpql에 의해 영속화가 풀릴 수 있으므로 명시적 merge
        reportStatusRepository.save(reportStatus);
    }

    private void handleSuspendUser(
            AuthenticatedUserInfo authenticatedUserInfo,
            ReportStatus reportStatus,
            SuspendUserRequest suspendUserRequest
    ) {
        // 정지 대상 유저 찾기
        User targetUser = null;
        if(reportStatus instanceof EventReportStatus) {
            targetUser = ((EventReportStatus) reportStatus).getEvent().getWriter();
        } else if(reportStatus instanceof RecruitmentReportStatus) {
            targetUser = ((RecruitmentReportStatus) reportStatus).getRecruitment().getWriter();
        } else if(reportStatus instanceof CommentReportStatus) {
            targetUser = ((CommentReportStatus) reportStatus).getPostComment().getUser();
        } else if(reportStatus instanceof CommentChildReportStatus) {
            targetUser = ((CommentChildReportStatus) reportStatus).getPostCommentChild().getUser();
        }
        // 대상 유저 정지
        userSuspensionService.suspendUser(
                authenticatedUserInfo.getUserId(),
                targetUser.getId(),
                suspendUserRequest
        );
        // 플래그 설정
        reportStatus.setReportedContentWriterSuspended(true);
    }
}
