package com.BaGulBaGul.BaGulBaGul.domain.post;

import static org.assertj.core.api.Assertions.assertThat;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request.PostRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostDetailInfo;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostSimpleInfo;

public class PostTestUtils {
    public static void assertPostDetailInfo(
            PostRegisterRequest postRegisterRequest,
            PostDetailInfo postDetailInfo
    ) {
        //게시글
        assertThat(postDetailInfo.getTitle()).isEqualTo(postRegisterRequest.getTitle());
        assertThat(postDetailInfo.getContent()).isEqualTo(postRegisterRequest.getContent());
        assertThat(postDetailInfo.getTags()).containsExactly(postRegisterRequest.getTags().toArray(String[]::new));
        assertThat(postDetailInfo.getImageIds()).containsExactly(postRegisterRequest.getImageIds().toArray(Long[]::new));
    }

    public static void assertPostSimpleInfo(
            PostRegisterRequest postRegisterRequest,
            PostSimpleInfo postSimpleInfo
    ) {
        //게시글
        assertThat(postSimpleInfo.getTitle()).isEqualTo(postRegisterRequest.getTitle());
        assertThat(postSimpleInfo.getTags()).containsExactly(postRegisterRequest.getTags().toArray(String[]::new));
    }
}
