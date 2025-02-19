package com.BaGulBaGul.BaGulBaGul.domain.ranking.applicationevent.listener;

import com.BaGulBaGul.BaGulBaGul.domain.event.applicationevent.QueryEventDetailByUserApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.service.EventRealtimeRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListenerForRanking {

    private final EventRealtimeRankingService eventRealtimeRankingService;

    //특정 이벤트를 유저가 상세조회 했을 시 처리
    @EventListener
    public void handleQueryEventDetailByUser(QueryEventDetailByUserApplicationEvent event) {
        eventRealtimeRankingService.increaseViews(
                event.getEventDetailResponse().getEvent().getEventId(),
                event.getEventDetailResponse().getEvent().getType()
        );
    }
}