package com.BaGulBaGul.BaGulBaGul.domain.event.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventBannerModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventBannerResponse;
import java.util.List;

public interface EventBannerService {
    List<EventBannerResponse> getEventBanners();
    void setEventBanners(List<EventBannerModifyRequest> bannerRegisterRequests);
}