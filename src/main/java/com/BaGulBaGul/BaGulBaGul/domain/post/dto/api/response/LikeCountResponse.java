package com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LikeCountResponse {
    private int likeCount;
}
