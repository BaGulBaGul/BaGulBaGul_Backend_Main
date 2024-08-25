package com.BaGulBaGul.BaGulBaGul.domain.post.dto.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RegisterPostCommentChildResult {
    Long postCommentChildId;
    Long validatedReplyTargetId;
}
