package com.BaGulBaGul.BaGulBaGul.domain.post.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class IsMyLikeResponse {
    @ApiModelProperty(value = "내가 좋아요를 눌렀는지 여부")
    private boolean isMyLike;
}
