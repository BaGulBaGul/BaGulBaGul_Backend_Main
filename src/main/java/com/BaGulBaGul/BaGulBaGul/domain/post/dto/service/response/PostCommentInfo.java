package com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.UserInfoResponse;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class PostCommentInfo {
    private Long commentId;
    private String content;
    private int commentChildCount;
    private int likeCount;
    private LocalDateTime createdAt;
    private UserInfoResponse writerInfo;
}
