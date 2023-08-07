package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostSimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    PostDetailResponse getPostDetailById(Long postId);
    Page<PostSimpleResponse> getPostPageByCondition(PostConditionalRequest postConditionalRequest, Pageable pageable);
    Long registerPost(Long userId, PostRegisterRequest postRegisterRequest);
}
