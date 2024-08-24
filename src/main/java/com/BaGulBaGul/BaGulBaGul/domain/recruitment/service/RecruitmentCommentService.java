package com.BaGulBaGul.BaGulBaGul.domain.recruitment.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetPostCommentChildPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetPostCommentPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentChildModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentChildRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentRegisterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecruitmentCommentService {

    PostCommentDetailResponse getPostCommentDetail(Long postCommentId);

    Page<GetPostCommentPageResponse> getRecruitmentCommentPage(Long recruitmentId, Long requestUserId, Pageable pageable);
    Page<GetPostCommentChildPageResponse> getRecruitmentCommentChildPage(Long commentId, Long requestUserId, Pageable pageable);

    Long registerComment(Long recruitmentId, Long userId, PostCommentRegisterRequest postCommentRegisterRequest);
    void modifyComment(Long commentId, Long userId, PostCommentModifyRequest postCommentModifyRequest);
    void deleteComment(Long commentId, Long userId);

    Long registerCommentChild(Long commentId, Long userId, PostCommentChildRegisterRequest postCommentChildRegisterRequest);
    void modifyCommentChild(Long commentChildId, Long userId, PostCommentChildModifyRequest postCommentChildModifyRequest);
    void deleteCommentChild(Long commentChildId, Long userId);

    void addLikeToComment(Long commentId, Long userId);
    void deleteLikeToComment(Long commentId, Long userId);
    boolean existsCommentLike(Long commentId, Long userId);

    void addLikeToCommentChild(Long commentChildId, Long userId);
    void deleteLikeToCommentChild(Long commentChildId, Long userId);
    boolean existsCommentChildLike(Long commentChildId, Long userId);
}
