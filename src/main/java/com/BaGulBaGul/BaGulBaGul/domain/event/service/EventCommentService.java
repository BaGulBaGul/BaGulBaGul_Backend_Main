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
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventCommentService {

    PostCommentDetailResponse getPostCommentDetail(Long postCommentId);

    Page<GetPostCommentPageResponse> getEventCommentPage(Long eventId, Long requestUserId, Pageable pageable);
    Page<GetPostCommentChildPageResponse> getEventCommentChildPage(Long commentId, Long requestUserId, Pageable pageable);

    Long registerComment(AuthenticatedUserInfo authenticatedUserInfo, Long eventId, PostCommentRegisterRequest postCommentRegisterRequest);
    void modifyComment(AuthenticatedUserInfo authenticatedUserInfo, Long commentId, PostCommentModifyRequest postCommentModifyRequest);
    void deleteComment(AuthenticatedUserInfo authenticatedUserInfo, Long commentId);

    Long registerCommentChild(AuthenticatedUserInfo authenticatedUserInfo, Long commentId, PostCommentChildRegisterRequest postCommentChildRegisterRequest);
    void modifyCommentChild(AuthenticatedUserInfo authenticatedUserInfo, Long commentChildId, PostCommentChildModifyRequest postCommentChildModifyRequest);
    void deleteCommentChild(AuthenticatedUserInfo authenticatedUserInfo, Long commentChildId);

    void addLikeToComment(Long commentId, Long userId) throws DuplicateLikeException;
    void deleteLikeToComment(Long commentId, Long userId) throws LikeNotExistException;
    boolean existsCommentLike(Long commentId, Long userId);

    void addLikeToCommentChild(Long commentChildId, Long userId) throws DuplicateLikeException;
    void deleteLikeToCommentChild(Long commentChildId, Long userId) throws LikeNotExistException;
    boolean existsCommentChildLike(Long commentChildId, Long userId);

    Long getEventIdFromCommentId(Long commentId);
}
