package com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.request;

import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.LocationModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.ParticipantStatusModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.PeriodModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request.PostModifyRequest;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventModifyApiRequest {

    @ApiModelProperty(value = "이벤트 타입 FESTIVAL, LOCAL_EVENT, PARTY 중 하나")
    private EventType type;

    @ApiModelProperty(value = "연령 제한 게시물 여부")
    private Boolean ageLimit;

    @ApiModelProperty(value = "등록할 카테고리의 이름들", example = "[\"스포츠/레저\",\"식품/음료\",\"문화/예술\"]")
    private List<String> categories;


    @ApiModelProperty(value = "이벤트 제목. 공백 불허")
    private String title;

    @ApiModelProperty(value = "게시글 내용")
    private String content;

    @ApiModelProperty(value = "태그들", example = "[\"물놀이\",\"바베큐\"]")
    private List<String> tags;

    @ApiModelProperty(value = "등록한 이미지들의 resource id. 순서는 보존되며 첫번째 이미지가 대표이미지가 된다.")
    private List<Long> imageIds;


    @ApiModelProperty(value = "세부 주소", example = "서울시 영등포구 xxx로 xxx타워 x층")
    @Builder.Default
    private JsonNullable<String> fullLocation = JsonNullable.undefined();

    @ApiModelProperty(value = "요약 주소", example = "서울시 영등포구")
    @Builder.Default
    private JsonNullable<String> abstractLocation = JsonNullable.undefined();

    @ApiModelProperty(value = "위도")
    @Builder.Default
    private JsonNullable<Float> latitudeLocation = JsonNullable.undefined();

    @ApiModelProperty(value = "경도")
    @Builder.Default
    private JsonNullable<Float> longitudeLocation = JsonNullable.undefined();


    @ApiModelProperty(value = "참여 인원")
    @Builder.Default
    private JsonNullable<Integer> currentHeadCount = JsonNullable.undefined();

    @ApiModelProperty(value = "모집 인원")
    @Builder.Default
    private JsonNullable<Integer> maxHeadCount = JsonNullable.undefined();


    @ApiModelProperty(value = "시작 시간")
    @Builder.Default
    private JsonNullable<LocalDateTime> startDate = JsonNullable.undefined();

    @ApiModelProperty(value = "종료 시간")
    @Builder.Default
    private JsonNullable<LocalDateTime> endDate = JsonNullable.undefined();


    public EventModifyRequest toEventModifyRequest() {
        return EventModifyRequest.builder()
                .type(type)
                .ageLimit(ageLimit)
                .categories(categories)
                .postModifyRequest(PostModifyRequest.builder()
                        .title(title)
                        .content(content)
                        .tags(tags)
                        .imageIds(imageIds)
                        .build())
                .locationModifyRequest(LocationModifyRequest.builder()
                        .fullLocation(fullLocation)
                        .abstractLocation(abstractLocation)
                        .latitudeLocation(latitudeLocation)
                        .longitudeLocation(longitudeLocation)
                        .build())
                .participantStatusModifyRequest(ParticipantStatusModifyRequest.builder()
                        .currentHeadCount(currentHeadCount)
                        .maxHeadCount(maxHeadCount)
                        .build())
                .periodModifyRequest(PeriodModifyRequest.builder()
                        .startDate(startDate)
                        .endDate(endDate)
                        .build())
                .build();
    }
}
