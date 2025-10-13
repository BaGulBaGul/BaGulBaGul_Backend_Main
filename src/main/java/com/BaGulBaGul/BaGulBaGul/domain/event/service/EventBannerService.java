package com.BaGulBaGul.BaGulBaGul.domain.event.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventBannerModifyRequest;
import java.util.List;

public interface EventBannerService {
    void setEventBanners(List<EventBannerModifyRequest> bannerRegisterRequests);
}