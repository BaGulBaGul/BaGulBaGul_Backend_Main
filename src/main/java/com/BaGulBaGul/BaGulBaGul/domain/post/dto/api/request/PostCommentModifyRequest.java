package com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
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
    @Size(min=1, message = "내용은 최소 {1}글자 이상이여야 합니다.")
    private String content;
}
