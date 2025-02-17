package com.BaGulBaGul.BaGulBaGul.domain.event.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventDetailResponse;

public interface EventStatisticsService {
    void handleViewByUser(EventDetailResponse eventDetailResponse);
}
