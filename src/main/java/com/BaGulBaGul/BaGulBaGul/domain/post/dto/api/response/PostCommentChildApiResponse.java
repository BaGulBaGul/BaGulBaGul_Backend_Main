package com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostCommentChildInfo;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.api.response.UserInfoApiResponse;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentChildApiResponse {

    @ApiModelProperty(value = "대댓글 id")
    private Long commentChildId;
    @ApiModelProperty(value = "댓글 id")
    private Long commentId;

    @ApiModelProperty(value = "내용")
    private String content;

    @ApiModelProperty(value = "좋아요 수")
    private int likeCount;

    @ApiModelProperty(value = "생성일")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "작성자 정보")
    private UserInfoApiResponse writerInfo;

    public static PostCommentChildApiResponse from(PostCommentChildInfo postCommentChildInfo) {
        return PostCommentChildApiResponse.builder()
                .commentChildId(postCommentChildInfo.getCommentChildId())
                .commentId(postCommentChildInfo.getCommentId())
                .content(postCommentChildInfo.getContent())
                .likeCount(postCommentChildInfo.getLikeCount())
                .createdAt(postCommentChildInfo.getCreatedAt())
                .writerInfo(UserInfoApiResponse.from(postCommentChildInfo.getWriterInfo()))
                .build();
    }
}
