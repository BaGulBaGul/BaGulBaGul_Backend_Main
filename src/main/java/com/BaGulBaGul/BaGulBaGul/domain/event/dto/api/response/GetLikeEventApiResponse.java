package com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.GetLikeEventResponse;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class GetLikeEventApiResponse {

    @ApiModelProperty(value = "이벤트 id")
    private Long eventId;

    @ApiModelProperty(value = "게시글 제목")
    private String title;

    @ApiModelProperty(value = "대표이미지 url")
    private String headImageUrl;

    @ApiModelProperty(value = "요약 주소", example = "서울시 영등포구")
    private String abstractLocation;

    @ApiModelProperty(value = "시작 시간")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "종료 시간")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "작성자 id")
    private Long eventWriterId;

    @ApiModelProperty(value = "작성자 프로필 이미지 url")
    private String eventWriterProfileImageUrl;

    public static GetLikeEventApiResponse from(GetLikeEventResponse getLikeEventResponse) {
        return GetLikeEventApiResponse.builder()
                .eventId(getLikeEventResponse.getEventId())
                .title(getLikeEventResponse.getTitle())
                .headImageUrl(getLikeEventResponse.getHeadImageUrl())
                .abstractLocation(getLikeEventResponse.getAbstractLocation())
                .startDate(getLikeEventResponse.getStartDate())
                .endDate(getLikeEventResponse.getEndDate())
                .eventWriterId(getLikeEventResponse.getEventWriterId())
                .eventWriterProfileImageUrl(getLikeEventResponse.getEventWriterProfileImageUrl())
                .build();
    }
}
