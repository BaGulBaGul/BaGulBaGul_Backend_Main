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
public class GetPostCommentChildPageResponse {
    private Long commentChildId;
    private Long userId;
    private String userName;
    private String content;
    private int likeCount;
    private boolean isMyLike;
    private LocalDateTime createdAt;
}
