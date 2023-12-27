package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.*;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostCommentService {
    PostCommentDetailResponse getPostCommentDetail(Long postCommentId);
    Page<GetPostCommentPageResponse> getPostCommentPage(Long postId, Long requestUserId, Pageable pageable);
    Page<GetPostCommentChildPageResponse> getPostCommentChildPage(Long postCommentId, Long requestUserId, Pageable pageable);
    Long registerPostComment(Long postId, Long userId, PostCommentRegisterRequest postCommentRegisterRequest);
    void modifyPostComment(Long postCommentId, Long userId, PostCommentModifyRequest postCommentModifyRequest);
    void deletePostComment(Long postCommentId, Long userId);
    Long registerPostCommentChild(Long postCommentId, Long userId, PostCommentChildRegisterRequest postCommentChildRegisterRequest);
    void modifyPostCommentChild(Long postCommentChildId, Long userId, PostCommentChildModifyRequest postCommentChildModifyRequest);
    void deletePostCommentChild(Long postCommentChildId, Long userId);
    void addLikeToComment(Long postCommentId, Long userId) throws DuplicateLikeException;
    void deleteLikeToComment(Long postCommentId, Long userId) throws LikeNotExistException;
    boolean existsCommentLike(Long postCommentId, Long userId);
    void addLikeToCommentChild(Long postCommentChildId, Long userId) throws DuplicateLikeException;
    void deleteLikeToCommentChild(Long postCommentChildId, Long userId) throws LikeNotExistException;
    boolean existsCommentChildLike(Long postCommentChildId, Long userId);
}
