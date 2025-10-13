package com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventBannerModifyRequest {
    Long targetEventBannerId;
    Long eventId;
    Long bannerImageResourceId;
}
