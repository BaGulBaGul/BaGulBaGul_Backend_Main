package com.BaGulBaGul.BaGulBaGul.domain.post.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class GetPostCommentPageResponse {
    private Long commentId;
    private Long userId;
    private String username;
    private String content;
    private int commentChildCount;
    private int likeCount;
    private boolean isMyLike;
    private LocalDateTime createdAt;
}
