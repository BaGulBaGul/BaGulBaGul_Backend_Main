package com.BaGulBaGul.BaGulBaGul.domain.event.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostStatisticsService;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.service.EventRealtimeRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventStatisticsServiceImpl implements EventStatisticsService {

    private final PostStatisticsService postStatisticsService;
    private final EventRealtimeRankingService eventRealtimeRankingService;

    @Override
    @Async
    public void handleViewByUser(EventDetailResponse eventDetailResponse) {
        //post 관련 통계 처리를 위임
        postStatisticsService.handleViewByUser(eventDetailResponse.getPost());
        //이벤트 조회수 실시간 랭킹 반영
        Long eventId = eventDetailResponse.getEvent().getEventId();
        EventType eventType = eventDetailResponse.getEvent().getType();
        eventRealtimeRankingService.increaseViews(eventId, eventType);
    }
}
