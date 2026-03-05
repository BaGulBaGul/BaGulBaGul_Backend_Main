package com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventDetailInfo;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventDetailResponse;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EventDetailApiInfo {
    @ApiModelProperty(value = "이벤트 id")
    private Long eventId;

    @ApiModelProperty(value = "이벤트 타입 FESTIVAL, LOCAL_EVENT, PARTY 중 하나")
    private EventType type;

    @ApiModelProperty(value = "주최자 유저 id")
    private Long eventHostUserId;

    @ApiModelProperty(value = "주최자 유저 닉네임")
    private String eventHostUserName;

    @ApiModelProperty(value = "주최자 프로필 이미지 url")
    private String eventHostUserProfileImageUrl;

    @ApiModelProperty(value = "참여 인원")
    private Integer currentHeadCount;

    @ApiModelProperty(value = "모집 인원")
    private Integer maxHeadCount;

    @ApiModelProperty(value = "세부 주소", example = "서울시 영등포구 xxx로 xxx타워 x층")
    private String fullLocation;

    @ApiModelProperty(value = "요약 주소", example = "서울시 영등포구")
    private String abstractLocation;

    @ApiModelProperty(value = "위도")
    private Float latitudeLocation;

    @ApiModelProperty(value = "경도")
    private Float longitudeLocation;

    @ApiModelProperty(value = "연령제한 게시물 여부")
    private Boolean ageLimit;

    @ApiModelProperty(value = "시작 시간")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "종료 시간")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "카테고리들", example = "[\"스포츠/레저\",\"식품/음료\",\"문화/예술\"]")
    private List<String> categories;

    public static EventDetailApiInfo from(EventDetailInfo eventDetailInfo) {
        return EventDetailApiInfo.builder()
                .eventId(eventDetailInfo.getEventId())
                .type(eventDetailInfo.getType())
                .eventHostUserId(eventDetailInfo.getEventHostUserId())
                .eventHostUserName(eventDetailInfo.getEventHostUserName())
                .eventHostUserProfileImageUrl(eventDetailInfo.getEventHostUserProfileImageUrl())
                .currentHeadCount(eventDetailInfo.getCurrentHeadCount())
                .maxHeadCount(eventDetailInfo.getMaxHeadCount())
                .fullLocation(eventDetailInfo.getFullLocation())
                .abstractLocation(eventDetailInfo.getAbstractLocation())
                .latitudeLocation(eventDetailInfo.getLatitudeLocation())
                .longitudeLocation(eventDetailInfo.getLongitudeLocation())
                .ageLimit(eventDetailInfo.getAgeLimit())
                .startDate(eventDetailInfo.getStartDate())
                .endDate(eventDetailInfo.getEndDate())
                .categories(eventDetailInfo.getCategories())
                .build();
    }
}
