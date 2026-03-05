package com.BaGulBaGul.BaGulBaGul.domain.admin.event.dto.api.request;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventBannerModifyRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventBannerModifyApiRequest {

    @ApiModelProperty(value = "카테고리 이름")
    private Long targetEventBannerId;
    @ApiModelProperty(value = "이벤트 id. null이면 무시")
    private Long eventId;
    @ApiModelProperty(value = "배너 이미지 id. null이면 무시")
    private Long eventBannerImageResourceId;

    public EventBannerModifyRequest toEventBannerModifyRequest() {
        return EventBannerModifyRequest.builder()
                .targetEventBannerId(targetEventBannerId)
                .eventId(eventId)
                .bannerImageResourceId(eventBannerImageResourceId)
                .build();
    }
}
