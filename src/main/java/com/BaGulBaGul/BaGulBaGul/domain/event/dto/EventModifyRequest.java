package com.BaGulBaGul.BaGulBaGul.domain.event.dto;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostModifyRequest;
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
public class EventModifyRequest {

    @ApiModelProperty(value = "이벤트 타입 FESTIVAL, LOCAL_EVENT, PARTY 중 하나")
    private EventType type;

    @ApiModelProperty(value = "이벤트 제목. 공백 불허")
    @Size(min=1, message = "제목은 공백이 아니여야 합니다.")
    private String title;

    @ApiModelProperty(value = "참여 인원")
    private JsonNullable<Integer> currentHeadCount = JsonNullable.undefined();

    @ApiModelProperty(value = "모집 인원")
    private JsonNullable<Integer> maxHeadCount = JsonNullable.undefined();

    @ApiModelProperty(value = "세부 주소", example = "서울시 영등포구 xxx로 xxx타워 x층")
    private String fullLocation;

    @ApiModelProperty(value = "요약 주소", example = "서울시 영등포구")
    private String abstractLocation;

    @ApiModelProperty(value = "위도")
    private Float latitudeLocation;

    @ApiModelProperty(value = "경도")
    private Float longitudeLocation;

    @ApiModelProperty(value = "게시글 내용")
    private String content;

    @ApiModelProperty(value = "연령 제한 게시물 여부")
    private Boolean ageLimit;

    @ApiModelProperty(value = "시작 시간")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "종료 시간")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "태그들", example = "[\"물놀이\",\"바베큐\"]")
    @Size(max = 10, message = "태그 개수는 {1}개 이하여야 합니다.")
    private List<String> tags;

    @ApiModelProperty(value = "등록할 카테고리의 이름들", example = "[\"스포츠/레저\",\"식품/음료\",\"문화/예술\"]")
    @Size(max = 2, message = "카테고리 개수는 {1}개 이하여야 합니다.")
    private List<String> categories;

    @ApiModelProperty(value = "등록한 이미지들의 resource id. 순서는 보존되며 첫번째 이미지가 대표이미지가 된다.")
    @Size(max = 10, message = "이미지 개수는 {1}개 이하여야 합니다.")
    private List<Long> imageIds;

    @AssertTrue(message = "이벤트 시작 날짜는 이벤트 종료 날짜보다 빨라야 합니다.")
    public boolean isStartDateBeforeEndDate() {
        return startDate == null || endDate == null || startDate.isBefore(endDate);
    }

    @AssertTrue(message = "참여 인원은 0명 이상이여야 합니다")
    public boolean isCurrentHeadCountNonNegative() {
        //값이 존재하지 않거나(json 필드에 명시하지 않음 = patch에 반영하지 않음)
        //값이 존재한다면 0명 이상이여야 함
        return !currentHeadCount.isPresent() || currentHeadCount.get() >= 0;
    }

    @AssertTrue(message = "모집 인원은 1명 이상이여야 합니다")
    public boolean isMaxHeadCountPositive() {
        //값이 존재하지 않거나(json 필드에 명시하지 않음 = patch에 반영하지 않음)
        //값이 존재한다면 1명 이상이여야 함
        return !maxHeadCount.isPresent() || maxHeadCount.get() >= 1;
    }



    public PostModifyRequest toPostModifyRequest() {
        return PostModifyRequest.builder()
                .title(title)
                .content(content)
                .tags(tags)
                .imageIds(imageIds)
                .build();
    }
}
