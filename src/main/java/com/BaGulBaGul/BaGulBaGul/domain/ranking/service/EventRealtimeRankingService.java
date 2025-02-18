package com.BaGulBaGul.BaGulBaGul.domain.ranking.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventSimpleResponse;
import java.util.List;

public interface EventRealtimeRankingService {
    //1위부터 count개수 만큼 조회
    List<EventSimpleResponse> getEventViewRanking(EventType eventType, int count);
}
