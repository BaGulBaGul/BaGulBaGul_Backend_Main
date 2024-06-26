package com.BaGulBaGul.BaGulBaGul.domain.post.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentChildModifyRequest {
    @ApiModelProperty(value = "내용")
    @Size(min=1, message = "내용은 최소 {1}글자 이상이여야 합니다.")
    private String content;

    @ApiModelProperty(value = "맨션 대상 유저 id. 삭제만 가능하다. null로 보낼 시 삭제. 이 외에는 무시.")
    private JsonNullable<Long> replyTargetUserId;
}
