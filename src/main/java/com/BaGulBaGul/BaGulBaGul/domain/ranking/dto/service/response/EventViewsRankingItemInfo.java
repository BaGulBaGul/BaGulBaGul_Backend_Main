package com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.service.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EventViewsRankingItemInfo {
    private Long eventId;
    private Long viewCount;
}