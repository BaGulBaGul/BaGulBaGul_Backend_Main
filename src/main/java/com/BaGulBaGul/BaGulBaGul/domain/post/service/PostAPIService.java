package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostAPIService {
    PostDetailResponse getPostDetailById(Long postId);
    Page<PostSimpleResponse> getPostPageByCondition(PostConditionalRequest postConditionalRequest, Pageable pageable);
    Long registerPost(Long userId, PostRegisterRequest postRegisterRequest);
    void modifyPost(Long postId, Long userId, PostModifyRequest postModifyRequest);
    void deletePost(Long postId, Long userId);

    void addLike(Long postId, Long userId) throws DuplicateLikeException;
    void deleteLike(Long postId, Long userId) throws LikeNotExistException;
    boolean isMyLike(Long postId, Long userId);
}
