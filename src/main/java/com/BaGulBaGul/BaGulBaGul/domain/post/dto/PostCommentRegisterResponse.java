package com.BaGulBaGul.BaGulBaGul.domain.post.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostCommentRegisterResponse {
    @ApiModelProperty(value = "댓글 id")
    private Long postCommentId;
}
