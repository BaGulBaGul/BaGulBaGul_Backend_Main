package com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request;

import javax.validation.constraints.NotNull;
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
    @NotNull
    Long targetEventBannerId;
    Long eventId;
    Long bannerImageResourceId;
}
