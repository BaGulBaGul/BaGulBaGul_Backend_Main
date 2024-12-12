package com.BaGulBaGul.BaGulBaGul.domain.recruitment.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostComment;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.GetPostCommentChildPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.GetPostCommentPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentChildModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentChildRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.PostCommentDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.RegisterPostCommentChildResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.PostCommentNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostCommentService;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent.NewRecruitmentCommentApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent.NewRecruitmentCommentChildApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent.NewRecruitmentCommentChildLikeApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent.NewRecruitmentCommentLikeApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.exception.RecruitmentNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecruitmentCommentServiceImpl implements RecruitmentCommentService {

    private final RecruitmentRepository recruitmentRepository;
    private final PostCommentRepository postCommentRepository;

    private final PostCommentService postCommentService;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public PostCommentDetailResponse getPostCommentDetail(
            Long postCommentId
    ) {
        return postCommentService.getPostCommentDetail(postCommentId);
    }

    @Override
    @Transactional
    public Page<GetPostCommentPageResponse> getRecruitmentCommentPage(
            Long recruitmentId,
            Long requestUserId,
            Pageable pageable
    ) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow(RecruitmentNotFoundException::new);
        Post post = recruitment.getPost();
        return postCommentService.getPostCommentPage(post.getId(), requestUserId, pageable);
    }

    @Override
    public Page<GetPostCommentChildPageResponse> getRecruitmentCommentChildPage(
            Long commentId,
            Long requestUserId,
            Pageable pageable
    ) {
        return postCommentService.getPostCommentChildPage(commentId, requestUserId, pageable);
    }

    @Override
    @Transactional
    public Long registerComment(
            Long recruitmentId,
            Long userId,
            PostCommentRegisterRequest postCommentRegisterRequest
    ) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow(RecruitmentNotFoundException::new);
        Post post = recruitment.getPost();
        //댓글 추가 후 id를 받아옴
        Long newCommentId = postCommentService.registerPostComment(post.getId(), userId, postCommentRegisterRequest);
        //댓글 추가 어플리케이션 이벤트 발행
        applicationEventPublisher.publishEvent(
                NewRecruitmentCommentApplicationEvent.builder()
                        .recruitmentId(recruitmentId)
                        .newCommentId(newCommentId)
                        .build()
        );
        return newCommentId;
    }

    @Override
    public void modifyComment(
            Long commentId,
            Long userId,
            PostCommentModifyRequest postCommentModifyRequest
    ) {
        postCommentService.modifyPostComment(commentId, userId, postCommentModifyRequest);
    }

    @Override
    public void deleteComment(
            Long commentId,
            Long userId
    ) {
        postCommentService.deletePostComment(commentId, userId);
    }

    @Override
    @Transactional
    public Long registerCommentChild(
            Long commentId,
            Long userId,
            PostCommentChildRegisterRequest postCommentChildRegisterRequest
    ) {
        //대댓글 추가 후 결과를 받아옴
        RegisterPostCommentChildResponse result = postCommentService.registerPostCommentChild(commentId, userId, postCommentChildRegisterRequest);
        //대댓글 추가 어플리케이션 이벤트 발행
        applicationEventPublisher.publishEvent(
                NewRecruitmentCommentChildApplicationEvent.builder()
                        .newCommentChildId(result.getPostCommentChildId())
                        .replyTargetCommentChildId(result.getValidatedReplyTargetId())
                        .build()
        );
        return result.getPostCommentChildId();
    }

    @Override
    public void modifyCommentChild(
            Long commentChildId,
            Long userId,
            PostCommentChildModifyRequest postCommentChildModifyRequest
    ) {
        postCommentService.modifyPostCommentChild(commentChildId, userId, postCommentChildModifyRequest);
    }

    @Override
    public void deleteCommentChild(
            Long commentChildId,
            Long userId
    ) {
        postCommentService.deletePostCommentChild(commentChildId, userId);
    }

    @Override
    @Transactional
    public void addLikeToComment(
            Long commentId,
            Long userId
    ) throws DuplicateLikeException {
        //댓글 좋아요 추가
        postCommentService.addLikeToComment(commentId, userId);
        //댓글 좋아요 추가 어플리케이션 이벤트 발행
        applicationEventPublisher.publishEvent(
                NewRecruitmentCommentLikeApplicationEvent.builder()
                        .likedCommentId(commentId)
                        .build()
        );
    }

    @Override
    public void deleteLikeToComment(
            Long commentId,
            Long userId
    ) throws LikeNotExistException {
        postCommentService.deleteLikeToComment(commentId, userId);
    }

    @Override
    public boolean existsCommentLike(
            Long commentId,
            Long userId
    ) {
        return postCommentService.existsCommentLike(commentId, userId);
    }

    @Override
    @Transactional
    public void addLikeToCommentChild(
            Long commentChildId,
            Long userId
    ) throws DuplicateLikeException {
        //대댓글 좋아요 추가
        postCommentService.addLikeToCommentChild(commentChildId, userId);
        //대댓글 좋아요 추가 어플리케이션 이벤트 발행
        applicationEventPublisher.publishEvent(
                NewRecruitmentCommentChildLikeApplicationEvent.builder()
                        .likedCommentChildId(commentChildId)
                        .build()
        );
    }

    @Override
    public void deleteLikeToCommentChild(
            Long commentChildId,
            Long userId
    ) throws LikeNotExistException {
        postCommentService.deleteLikeToCommentChild(commentChildId, userId);
    }

    @Override
    public boolean existsCommentChildLike(
            Long commentChildId,
            Long userId
    ) {
        return postCommentService.existsCommentChildLike(commentChildId, userId);
    }

    @Override
    @Transactional
    public Long getRecruitmentIdFromCommentId(Long commentId) {
        PostComment postComment = postCommentRepository.findById(commentId).orElseThrow(PostCommentNotFoundException::new);
        Recruitment recruitment = recruitmentRepository.findByPost(postComment.getPost());
        if(recruitment == null) {
            throw new RecruitmentNotFoundException();
        }
        return recruitment.getId();
    }
}
