package com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TagRankingItemInfo {
    private String tag;
    private Long referenceCount;
}
