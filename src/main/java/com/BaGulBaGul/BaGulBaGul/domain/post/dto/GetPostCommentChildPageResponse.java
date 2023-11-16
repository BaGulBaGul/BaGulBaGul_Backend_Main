package com.BaGulBaGul.BaGulBaGul.domain.post.dto;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "대댓글 id")
    private Long commentChildId;

    @ApiModelProperty(value = "작성자 유저 id")
    private Long userId;

    @ApiModelProperty(value = "작성자 닉네임")
    private String userName;

    @ApiModelProperty(value = "내용")
    private String content;

    @ApiModelProperty(value = "좋아요 수")
    private int likeCount;

    @ApiModelProperty(value = "좋아요를 눌렀는지 여부. 로그인하지 않았다면 무조건 false")
    private boolean isMyLike;

    @ApiModelProperty(value = "생성일")
    private LocalDateTime createdAt;
}
