package com.BaGulBaGul.BaGulBaGul.domain.ranking.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventViewRankingRepository;
import com.BaGulBaGul.BaGulBaGul.domain.event.service.EventService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventRealtimeRankingServiceImpl implements EventRealtimeRankingService {

    private final EventViewRankingRepository eventViewRankingRepository;

    private final EventService eventService;

    @Override
    public List<EventSimpleResponse> getEventViewRanking(EventType eventType, int count) {
        List<Long> eventIds = eventViewRankingRepository.getTopKRankEventFrom7DaysViewCount(eventType, count);
        return eventService.getEventSimpleResponseByIds(eventIds);
    }
}
