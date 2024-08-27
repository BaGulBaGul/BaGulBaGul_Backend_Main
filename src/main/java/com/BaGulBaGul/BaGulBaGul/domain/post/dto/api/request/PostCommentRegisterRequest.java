package com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
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
public class PostCommentRegisterRequest {
    @ApiModelProperty(value = "내용 | 필수, 공백 불허")
    @NotBlank(message = "내용은 null이거나 공백이여서는 안됩니다.")
    private String content;
}
