package com.BaGulBaGul.BaGulBaGul.domain.event.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventStatisticsServiceImpl implements EventStatisticsService {

    private final PostStatisticsService postStatisticsService;

    @Override
    @Async
    public void handleViewByUser(EventDetailResponse eventDetailResponse) {
        postStatisticsService.handleViewByUser(eventDetailResponse.getPost());
    }
}
