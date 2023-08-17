package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetPostCommentPageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostCommentAPIService {
    Page<GetPostCommentPageResponse> getPostCommentPage(Long postId, Long requestUserId, Pageable pageable);
}
