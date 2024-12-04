package com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventDetailInfo;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostDetailInfo;
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
    private EventDetailInfo event;

    @ApiModelProperty(value = "게시글 정보")
    private PostDetailInfo post;

    public static EventDetailApiResponse from(EventDetailResponse eventDetailResponse) {
        return EventDetailApiResponse.builder()
                .event(eventDetailResponse.getEvent())
                .post(eventDetailResponse.getPost())
                .build();
    }
}
