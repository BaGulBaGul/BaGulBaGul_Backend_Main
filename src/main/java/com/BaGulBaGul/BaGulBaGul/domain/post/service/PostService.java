package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostDetailResponse;

public interface PostService {
    PostDetailResponse getPostDetailById(Long postId);
}
