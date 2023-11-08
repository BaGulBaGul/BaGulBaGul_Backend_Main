package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class IsMyLikeResponse {
    private boolean isMyLike;
}
