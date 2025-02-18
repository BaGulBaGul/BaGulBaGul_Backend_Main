package com.BaGulBaGul.BaGulBaGul.domain.event.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventViewRankingRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostStatisticsService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventStatisticsServiceImpl implements EventStatisticsService {

    private final PostStatisticsService postStatisticsService;
    private final EventViewRankingRepository eventViewRankingRepository;

    @Override
    @Async
    public void handleViewByUser(EventDetailResponse eventDetailResponse) {
        //post 관련 통계 처리를 위임
        postStatisticsService.handleViewByUser(eventDetailResponse.getPost());
        //이벤트 조회수 실시간 랭킹 반영
        Long eventId = eventDetailResponse.getEvent().getEventId();
        EventType eventType = eventDetailResponse.getEvent().getType();
        applyToRealtimeEventViewRanking(eventId, eventType);
    }

    private void applyToRealtimeEventViewRanking(Long eventId, EventType eventType) {
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
