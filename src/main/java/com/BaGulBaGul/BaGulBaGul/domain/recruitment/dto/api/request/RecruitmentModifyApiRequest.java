package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.request;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.constant.RecruitmentState;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request.RecruitmentModifyRequest;
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
public class RecruitmentModifyApiRequest {

    @ApiModelProperty(value = "모집 상태")
    private RecruitmentState state;

    @ApiModelProperty(value = "참여 인원")
    private JsonNullable<Integer> currentHeadCount = JsonNullable.undefined();

    @ApiModelProperty(value = "모집 인원")
    private JsonNullable<Integer> maxHeadCount = JsonNullable.undefined();

    @ApiModelProperty(value = "시작 시간")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "종료 시간")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "게시글 제목")
    @Size(min=1, message = "제목은 공백이 아니여야 합니다.")
    private String title;

    @ApiModelProperty(value = "게시글 내용")
    private String content;

    @ApiModelProperty(value = "태그들", example = "[\"물놀이\",\"바베큐\"]")
    @Size(max = 10, message = "태그 개수는 {1}개 이하여야 합니다.")
    private List<String> tags;

    @ApiModelProperty(value = "등록한 이미지들의 resource id. 순서는 보존되며 첫번째 이미지가 대표이미지가 된다.")
    @Size(max = 10, message = "이미지 개수는 {1}개 이하여야 합니다.")
    private List<Long> imageIds;

    @AssertTrue(message = "모집 시작 날짜는 모집 종료 날짜보다 빨라야 합니다.")
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


    public RecruitmentModifyRequest toRecruitmentModifyRequest() {
        throw new UnsupportedOperationException();
    }
}
