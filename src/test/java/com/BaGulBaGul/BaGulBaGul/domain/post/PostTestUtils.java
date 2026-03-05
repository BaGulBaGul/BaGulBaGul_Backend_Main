package com.BaGulBaGul.BaGulBaGul.domain.post;

import static org.assertj.core.api.Assertions.assertThat;

import com.BaGulBaGul.BaGulBaGul.domain.post.PostComment;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentChild;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request.PostRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostCommentChildInfo;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostCommentInfo;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostDetailInfo;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostSimpleInfo;
import com.BaGulBaGul.BaGulBaGul.domain.user.UserTestUtils;

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

    public static void assertPostSimpleInfo(
            PostSimpleInfo postSimpleInfo,
            Post post
    ) {
        //게시글
        assertThat(postSimpleInfo.getTitle()).isEqualTo(post.getTitle());
        assertThat(postSimpleInfo.getTags()).containsExactly(post.getTags().split(" "));
    }

    public static void assertPostCommentInfo(PostCommentInfo info, PostComment comment) {
        assertThat(info.getCommentId()).isEqualTo(comment.getId());
        assertThat(info.getContent()).isEqualTo(comment.getContent());
        assertThat(info.getCommentChildCount()).isEqualTo(comment.getCommentChildCount());
        assertThat(info.getLikeCount()).isEqualTo(comment.getLikeCount());
        assertThat(info.getCreatedAt()).isEqualTo(comment.getCreatedAt());
        UserTestUtils.assertUserInfoResponse(info.getWriterInfo(), comment.getUser());
    }

    public static void assertPostCommentChildInfo(PostCommentChildInfo info, PostCommentChild commentChild) {
        assertThat(info.getCommentChildId()).isEqualTo(commentChild.getId());
        assertThat(info.getCommentId()).isEqualTo(commentChild.getPostComment().getId());
        assertThat(info.getContent()).isEqualTo(commentChild.getContent());
        assertThat(info.getLikeCount()).isEqualTo(commentChild.getLikeCount());
        assertThat(info.getCreatedAt()).isEqualTo(commentChild.getCreatedAt());
        UserTestUtils.assertUserInfoResponse(info.getWriterInfo(), commentChild.getUser());
    }
}
