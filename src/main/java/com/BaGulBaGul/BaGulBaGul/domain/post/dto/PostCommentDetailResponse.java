package com.BaGulBaGul.BaGulBaGul.domain.post.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class PostCommentDetailResponse {

    @ApiModelProperty(value = "댓글 id")
    private Long commentId;

    @ApiModelProperty(value = "작성자 유저 id")
    private Long userId;

    @ApiModelProperty(value = "작성자 닉네임")
    private String username;

    @ApiModelProperty(value = "내용")
    private String content;

    @ApiModelProperty(value = "대댓글 수")
    private int commentChildCount;

    @ApiModelProperty(value = "좋아요 수")
    private int likeCount;

    @ApiModelProperty(value = "생성일")
    private LocalDateTime createdAt;
}
