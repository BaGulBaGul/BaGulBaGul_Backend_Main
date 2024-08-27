package com.BaGulBaGul.BaGulBaGul.domain.event.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.GetPostCommentChildPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.GetPostCommentPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentChildModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentChildRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.PostCommentDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventCommentService {

    PostCommentDetailResponse getPostCommentDetail(Long postCommentId);

    Page<GetPostCommentPageResponse> getEventCommentPage(Long eventId, Long requestUserId, Pageable pageable);
    Page<GetPostCommentChildPageResponse> getEventCommentChildPage(Long commentId, Long requestUserId, Pageable pageable);

    Long registerComment(Long eventId, Long userId, PostCommentRegisterRequest postCommentRegisterRequest);
    void modifyComment(Long commentId, Long userId, PostCommentModifyRequest postCommentModifyRequest);
    void deleteComment(Long commentId, Long userId);

    Long registerCommentChild(Long commentId, Long userId, PostCommentChildRegisterRequest postCommentChildRegisterRequest);
    void modifyCommentChild(Long commentChildId, Long userId, PostCommentChildModifyRequest postCommentChildModifyRequest);
    void deleteCommentChild(Long commentChildId, Long userId);

    void addLikeToComment(Long commentId, Long userId) throws DuplicateLikeException;
    void deleteLikeToComment(Long commentId, Long userId) throws LikeNotExistException;
    boolean existsCommentLike(Long commentId, Long userId);

    void addLikeToCommentChild(Long commentChildId, Long userId) throws DuplicateLikeException;
    void deleteLikeToCommentChild(Long commentChildId, Long userId) throws LikeNotExistException;
    boolean existsCommentChildLike(Long commentChildId, Long userId);

    Long getEventIdFromCommentId(Long commentId);
}
