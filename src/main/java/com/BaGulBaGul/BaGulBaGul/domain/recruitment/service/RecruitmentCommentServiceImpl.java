package com.BaGulBaGul.BaGulBaGul.domain.recruitment.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.GetPostCommentChildPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.GetPostCommentPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentChildModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentChildRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.PostCommentDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostCommentService;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.exception.RecruitmentNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecruitmentCommentServiceImpl implements RecruitmentCommentService {

    private final RecruitmentRepository recruitmentRepository;

    private final PostCommentService postCommentService;

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
    public Long registerComment(
            Long recruitmentId,
            Long userId,
            PostCommentRegisterRequest postCommentRegisterRequest
    ) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow(RecruitmentNotFoundException::new);
        Post post = recruitment.getPost();
        return postCommentService.registerPostComment(post.getId(), userId, postCommentRegisterRequest);
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
    public Long registerCommentChild(
            Long commentId,
            Long userId,
            PostCommentChildRegisterRequest postCommentChildRegisterRequest
    ) {
        return postCommentService.registerPostCommentChild(commentId, userId, postCommentChildRegisterRequest);
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
    public void addLikeToComment(
            Long commentId,
            Long userId
    ) {
        try {
            postCommentService.addLikeToComment(commentId, userId);
        }
        catch (DuplicateLikeException e) {
        }
    }

    @Override
    public void deleteLikeToComment(
            Long commentId,
            Long userId
    ) {
        try {
            postCommentService.deleteLikeToComment(commentId, userId);
        }
        catch (LikeNotExistException e) {
        }
    }

    @Override
    public boolean existsCommentLike(
            Long commentId,
            Long userId
    ) {
        return postCommentService.existsCommentLike(commentId, userId);
    }

    @Override
    public void addLikeToCommentChild(
            Long commentChildId,
            Long userId
    ) {
        try {
            postCommentService.addLikeToCommentChild(commentChildId, userId);
        }
        catch (DuplicateLikeException e) {
        }
    }

    @Override
    public void deleteLikeToCommentChild(
            Long commentChildId,
            Long userId
    ) {
        try {
            postCommentService.deleteLikeToCommentChild(commentChildId, userId);
        }
        catch (LikeNotExistException e) {
        }
    }

    @Override
    public boolean existsCommentChildLike(
            Long commentChildId,
            Long userId
    ) {
        return postCommentService.existsCommentChildLike(commentChildId, userId);
    }
}
