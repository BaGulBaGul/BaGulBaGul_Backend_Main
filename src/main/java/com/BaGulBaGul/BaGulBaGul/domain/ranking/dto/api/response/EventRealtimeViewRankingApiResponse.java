package com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventSimpleResponse;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRealtimeViewRankingApiResponse {

    @ApiModelProperty(value = "이벤트 id")
    private Long eventId;

    @ApiModelProperty(value = "게시글 제목")
    private String title;

    @ApiModelProperty(value = "대표이미지 경로")
    private String headImageUrl;

    @ApiModelProperty(value = "시작 시간")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "종료 시간")
    private LocalDateTime endDate;

    public static EventRealtimeViewRankingApiResponse from(EventSimpleResponse eventSimpleResponse) {
        return EventRealtimeViewRankingApiResponse.builder()
                .eventId(eventSimpleResponse.getEvent().getEventId())
                .title(eventSimpleResponse.getPost().getTitle())
                .headImageUrl(eventSimpleResponse.getPost().getHeadImageUrl())
                .startDate(eventSimpleResponse.getEvent().getStartDate())
                .endDate(eventSimpleResponse.getEvent().getEndDate())
                .build();
    }

}