package com.BaGulBaGul.BaGulBaGul.domain.post.dto;

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
public class PostCommentChildRegisterRequest {
    @ApiModelProperty(value = "내용 | 필수, 공백 불허")
    @NotBlank
    private String content;

    @ApiModelProperty(value = "작성 대댓글이 답글이 아닐 경우 null, 답글일 경우 답장을 받을 대댓글의 id.\n"
            + "같은 댓글 안의 대댓글에만 답장을 쓸 수 있다. 이외에는 검증 후 무시됨")
    private Long replyTargetPostCommentChildId;
}
