package com.BaGulBaGul.BaGulBaGul.domain.event.dto;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
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
public class GetLikeEventResponse {

    @ApiModelProperty(value = "이벤트 id")
    private Long id;

    @ApiModelProperty(value = "게시글 제목")
    private String title;

    @ApiModelProperty(value = "요약 주소", example = "서울시 영등포구")
    private String abstractLocation;

    @ApiModelProperty(value = "시작 시간")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "종료 시간")
    private LocalDateTime endDate;

    public static GetLikeEventResponse of(Event event) {
        return GetLikeEventResponse.builder()
                .id(event.getId())
                .title(event.getPost().getTitle())
                .abstractLocation(event.getAbstractLocation())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .build();
    }
}
