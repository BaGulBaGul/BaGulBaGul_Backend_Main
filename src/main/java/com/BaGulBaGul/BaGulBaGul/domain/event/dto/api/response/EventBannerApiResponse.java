package com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventBannerResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventSimpleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class EventBannerApiResponse {
    Long eventBannerId;
    String eventBannerImageUrl;
    EventPageApiResponse event;

    public static EventBannerApiResponse from(EventBannerResponse eventBannerResponse) {
        return EventBannerApiResponse.builder()
                .eventBannerId(eventBannerResponse.getEventBannerId())
                .eventBannerImageUrl(eventBannerResponse.getEventBannerImageUrl())
                .event(EventPageApiResponse.from(eventBannerResponse.getEventSimpleResponse()))
                .build();
    }
}
