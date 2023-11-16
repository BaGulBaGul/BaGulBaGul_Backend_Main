package com.BaGulBaGul.BaGulBaGul.domain.post.dto;

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
public class PostCommentModifyRequest {
    @ApiModelProperty(value = "내용")
    private String content;
}
