package com.BaGulBaGul.BaGulBaGul.domain.ranking.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.applicationevent.QueryEventDetailByUserApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventSimpleResponse;
import java.util.List;

public interface EventRealtimeRankingService {
    //1위부터 count개수 만큼 조회
    List<EventSimpleResponse> getEventViewRanking(EventType eventType, int count);
    //특정 이벤트의 조회수 증가
    void increaseViews(Long eventId, EventType eventType);
}
