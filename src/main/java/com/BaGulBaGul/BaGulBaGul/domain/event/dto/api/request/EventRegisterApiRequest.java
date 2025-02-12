package com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.request;

import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.LocationRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.ParticipantStatusRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.PeriodRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request.PostRegisterRequest;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;
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
public class EventRegisterApiRequest {

    @ApiModelProperty(value = "이벤트 타입 FESTIVAL, LOCAL_EVENT, PARTY 중 하나 | 필수")
    private EventType type;

    @ApiModelProperty(value = "연령 제한 게시물 여부")
    private Boolean ageLimit;

    @ApiModelProperty(value = "등록할 카테고리의 이름들", example = "[\"스포츠/레저\",\"식품/음료\",\"문화/예술\"]")
    private List<String> categories;


    @ApiModelProperty(value = "이벤트 제목 | 필수, 공백 불허")
    private String title;

    @ApiModelProperty(value = "게시글 내용")
    private String content;

    @ApiModelProperty(value = "태그들", example = "[\"물놀이\",\"바베큐\"]")
    private List<String> tags;

    @ApiModelProperty(value = "등록한 이미지들의 resource id. 순서는 보존되며 첫번째 이미지가 대표이미지가 된다.")
    private List<Long> imageIds;


    @ApiModelProperty(value = "세부 주소", example = "서울시 영등포구 xxx로 xxx타워 x층")
    private String fullLocation;

    @ApiModelProperty(value = "요약 주소", example = "서울시 영등포구")
    private String abstractLocation;

    @ApiModelProperty(value = "위도")
    private Float latitudeLocation;

    @ApiModelProperty(value = "경도")
    private Float longitudeLocation;


    @ApiModelProperty(value = "모집 인원")
    private Integer maxHeadCount;
    @ApiModelProperty(value = "현재 인원")
    private Integer currentHeadCount;


    @ApiModelProperty(value = "시작 시간")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "종료 시간")
    private LocalDateTime endDate;


    public EventRegisterRequest toEventRegisterRequest() {
        return EventRegisterRequest.builder()
                .type(type)
                .ageLimit(ageLimit)
                .categories(categories)
                .postRegisterRequest(PostRegisterRequest.builder()
                        .title(title)
                        .content(content)
                        .tags(tags)
                        .imageIds(imageIds)
                        .build())
                .locationRegisterRequest(LocationRegisterRequest.builder()
                        .fullLocation(fullLocation)
                        .abstractLocation(abstractLocation)
                        .latitudeLocation(latitudeLocation)
                        .longitudeLocation(longitudeLocation)
                        .build())
                .participantStatusRegisterRequest(ParticipantStatusRegisterRequest.builder()
                        .currentHeadCount(currentHeadCount)
                        .maxHeadCount(maxHeadCount)
                        .build())
                .periodRegisterRequest(PeriodRegisterRequest.builder()
                        .startDate(startDate)
                        .endDate(endDate)
                        .build())
                .build();
    }
}
