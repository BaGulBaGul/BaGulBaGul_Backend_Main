package com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response;

import io.swagger.annotations.ApiModelProperty;
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
public class PostCommentChildRegisterResponse {
    @ApiModelProperty(value = "대댓글 id")
    private Long postCommentChildId;
}
