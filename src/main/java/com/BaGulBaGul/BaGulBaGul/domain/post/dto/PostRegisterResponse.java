package com.BaGulBaGul.BaGulBaGul.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostRegisterResponse {
    private Long postId;
}
