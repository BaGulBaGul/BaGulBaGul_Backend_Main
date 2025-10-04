package com.BaGulBaGul.BaGulBaGul.domain.report.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventRepository;
import com.BaGulBaGul.BaGulBaGul.domain.event.service.EventService;
import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostComment;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentChild;
import com.BaGulBaGul.BaGulBaGul.domain.post.constant.PostType;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostCommentChildInfo;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostCommentInfo;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentChildRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostCommentService;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response.RecruitmentSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.RecruitmentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.service.RecruitmentService;
import com.BaGulBaGul.BaGulBaGul.domain.report.CommentChildReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.CommentReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.EventReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.RecruitmentReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.ReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportContentType;
import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportStatusState;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.request.CompleteReportStatusRequest;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.request.FindReportStatusByConditionRequest;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.response.FindReportStatusByConditionResponse;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.response.ReportStatusInfo;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.response.ReportStatusOriginalContentInfo;
import com.BaGulBaGul.BaGulBaGul.domain.report.exception.ReportStatusNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.report.repository.ReportStatusRepository;
import com.BaGulBaGul.BaGulBaGul.domain.report.repository.querydsl.FindReportStatusByCondition.ReportStatusIdsWithTotalCountOfPageResult;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.SuspendUserRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserInfoService;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserSuspensionService;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.swing.text.html.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    private final PostCommentRepository postCommentRepository;
    private final PostCommentChildRepository postCommentChildRepository;

    @Override
    @Transactional
    public Page<FindReportStatusByConditionResponse> findReportStatusByCondition(
            FindReportStatusByConditionRequest conditionRequest, Pageable pageable) {
        ReportStatusIdsWithTotalCountOfPageResult findResult = reportStatusRepository.findReportStatusIdsByConditionAndPageable(
                conditionRequest, pageable);
        List<FindReportStatusByConditionResponse> responseContent = getConditionResponseWithFetch(
                findResult.getReportStatuses());
        return new PageImpl<>(responseContent, pageable, findResult.getTotalCount());
    }
    @Override
    @Transactional
    public List<FindReportStatusByConditionResponse> getConditionResponseWithFetch(List<ReportStatus> reportStatuses) {
        List<Long> eventIds = new ArrayList<>();
        List<Long> recruitmentIds = new ArrayList<>();
        List<Long> commentIds = new ArrayList<>();
        List<Long> commentChildIds = new ArrayList<>();

        for(ReportStatus reportStatus : reportStatuses) {
            if(reportStatus instanceof EventReportStatus) {
                eventIds.add(((EventReportStatus) reportStatus).getEvent().getId());
            } else if(reportStatus instanceof RecruitmentReportStatus) {
                recruitmentIds.add(((RecruitmentReportStatus) reportStatus).getRecruitment().getId());
            } else if(reportStatus instanceof CommentReportStatus) {
                commentIds.add(((CommentReportStatus) reportStatus).getPostComment().getId());
            } else if(reportStatus instanceof CommentChildReportStatus) {
                commentChildIds.add(((CommentChildReportStatus) reportStatus).getPostCommentChild().getId());
            }
        }

        //댓글 패치 조인

        if(commentIds != null && commentIds.size() > 0) {
            postCommentRepository.findWithCommentWriterAndPostByIds(commentIds);
        }
        //댓글이 속한 게시물의 id를 이벤트나 모집글에 추가
        eventIds.addAll(
                commentIds.stream().map(postCommentRepository::getReferenceById).map(PostComment::getPost)
                        .filter(post -> post.getType()== PostType.Event).map(Post::getEvent).map(Event::getId)
                        .collect(Collectors.toList())
        );
        recruitmentIds.addAll(
                commentIds.stream().map(postCommentRepository::getReferenceById).map(PostComment::getPost)
                        .filter(post -> post.getType()== PostType.Recruitment).map(Post::getRecruitment)
                        .map(Recruitment::getId).collect(Collectors.toList())
        );
        //대댓글 패치 조인
        if(commentChildIds != null && commentChildIds.size() > 0) {
            postCommentChildRepository.findWithCommentChildWriterAndCommentAndPostByIds(commentChildIds);
        }
        //대댓글이 속한 게시물의 id를 이벤트나 모집글에 추가
        eventIds.addAll(
                commentChildIds.stream().map(postCommentChildRepository::getReferenceById)
                        .map(PostCommentChild::getPostComment).map(PostComment::getPost)
                        .filter(post -> post.getType()== PostType.Event).map(Post::getEvent).map(Event::getId)
                        .collect(Collectors.toList())
        );
        recruitmentIds.addAll(
                commentChildIds.stream().map(postCommentChildRepository::getReferenceById)
                        .map(PostCommentChild::getPostComment).map(PostComment::getPost)
                        .filter(post -> post.getType()== PostType.Recruitment).map(Post::getRecruitment)
                        .map(Recruitment::getId).collect(Collectors.toList())
        );
        //이벤트 패치 조인
        eventService.fetchForEventSimpleResponse(eventIds);
        //모집글 패치 조인
        recruitmentService.fetchForRecruitmentSimpleResponse(recruitmentIds);

        //dto 로 변환
        List<FindReportStatusByConditionResponse> result = new ArrayList<>();
        for(ReportStatus reportStatus : reportStatuses) {
            // 공통 데이터
            FindReportStatusByConditionResponse response = FindReportStatusByConditionResponse.builder()
                    .reportStatusInfo(ReportStatusInfo.from(reportStatus))
                    .build();
            // 신고 대상 게시물 데이터
            response.setReportStatusOriginalContentInfo(getReportStatusOriginalContentInfo(reportStatus));
            // 결과 리스트에 추가
            result.add(response);
        }

        return result;
    }

    private ReportStatusOriginalContentInfo getReportStatusOriginalContentInfo(ReportStatus reportStatus) {
        ReportStatusOriginalContentInfo result = ReportStatusOriginalContentInfo.builder().build();
        // 게시물 종류 별 데이터 채우기
        if(reportStatus instanceof EventReportStatus) {
            EventReportStatus eventReportStatus = (EventReportStatus) reportStatus;
            EventSimpleResponse eventSimpleResponse = eventService
                    .getEventSimpleById(eventReportStatus.getEvent().getId());
            result.setEventInfo(Optional.of(eventSimpleResponse));
        } else if(reportStatus instanceof RecruitmentReportStatus) {
            RecruitmentReportStatus recruitmentReportStatus = (RecruitmentReportStatus) reportStatus;
            RecruitmentSimpleResponse recruitmentSimpleResponse = recruitmentService
                    .getRecruitmentSimpleResponseById(recruitmentReportStatus.getRecruitment().getId());
            result.setRecruitmentInfo(Optional.of(recruitmentSimpleResponse));
        } else if(reportStatus instanceof CommentReportStatus) {
            CommentReportStatus commentReportStatus = (CommentReportStatus) reportStatus;
            PostCommentInfo postCommentInfo = postCommentService
                    .getPostCommentInfo(commentReportStatus.getPostComment().getId());
            result.setCommentInfo(Optional.of(postCommentInfo));
        } else if(reportStatus instanceof CommentChildReportStatus) {
            CommentChildReportStatus commentChildReportStatus = (CommentChildReportStatus) reportStatus;
            PostCommentChildInfo postCommentChildInfo = postCommentService.getPostCommentChildInfo(
                    commentChildReportStatus.getPostCommentChild().getId());
            result.setCommentChildInfo(Optional.of(postCommentChildInfo));
        }
        return result;
    }

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
