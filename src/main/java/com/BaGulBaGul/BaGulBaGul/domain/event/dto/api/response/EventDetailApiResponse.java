package com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventDetailInfo;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.PostDetailApiInfo;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostDetailInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class EventDetailApiResponse {

    @ApiModelProperty(value = "이벤트 정보")
    private EventDetailApiInfo event;

    @ApiModelProperty(value = "게시글 정보")
    private PostDetailApiInfo post;

    public static EventDetailApiResponse from(EventDetailResponse eventDetailResponse) {
        return EventDetailApiResponse.builder()
                .event(EventDetailApiInfo.from(eventDetailResponse.getEvent()))
                .post(PostDetailApiInfo.from(eventDetailResponse.getPost()))
                .build();
    }
}
