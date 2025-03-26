package com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RegisterPostCommentChildResponse {
    Long postCommentChildId;
    Long validatedReplyTargetId;
}
