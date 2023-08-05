package com.BaGulBaGul.BaGulBaGul.domain.post.repository.queryDSL;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostSimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindPostByCondition {
    Page<PostSimpleResponse> findPostSimpleResponsePageByCondition(
            PostConditionalRequest postConditionalRequest, Pageable pageable
    );
}
