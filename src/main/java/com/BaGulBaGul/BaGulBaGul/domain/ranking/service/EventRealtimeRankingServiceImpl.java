package com.BaGulBaGul.BaGulBaGul.domain.ranking.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.applicationevent.QueryEventDetailByUserApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.repository.EventViewRankingRepository;
import com.BaGulBaGul.BaGulBaGul.domain.event.service.EventService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
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

    @Override
    public void increaseViews(Long eventId, EventType eventType) {
        //오늘 날짜의 조회수 증가
        eventViewRankingRepository.increaseDayViewCount(
                eventId,
                eventType,
                LocalDateTime.now(),
                1L
        );
        //최근 7일간의 조회수 증가
        eventViewRankingRepository.increase7DaysViewCount(
                eventId,
                eventType,
                1L
        );
    }
}
