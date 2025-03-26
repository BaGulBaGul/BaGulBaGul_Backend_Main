package com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventSimpleInfo;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostSimpleInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class EventPageApiResponse {

    @ApiModelProperty(value = "이벤트 정보")
    private EventSimpleInfo event;

    @ApiModelProperty(value = "게시글 정보")
    private PostSimpleInfo post;

    public static EventPageApiResponse from(EventSimpleResponse eventSimpleResponse) {
        return EventPageApiResponse.builder()
                .event(eventSimpleResponse.getEvent())
                .post(eventSimpleResponse.getPost())
                .build();
    }
}
