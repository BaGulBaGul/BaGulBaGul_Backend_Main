package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.request;

import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.ParticipantStatusModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.PeriodModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request.PostModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.constant.RecruitmentState;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request.RecruitmentModifyRequest;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
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

    @ApiModelProperty(value = "게시글 제목")
    private String title;

    @ApiModelProperty(value = "게시글 내용")
    private String content;

    @ApiModelProperty(value = "태그들", example = "[\"물놀이\",\"바베큐\"]")
    private List<String> tags;

    @ApiModelProperty(value = "등록한 이미지들의 resource id. 순서는 보존되며 첫번째 이미지가 대표이미지가 된다.")
    private List<Long> imageIds;

    public RecruitmentModifyRequest toRecruitmentModifyRequest() {
        return RecruitmentModifyRequest.builder()
                .state(state)
                .periodModifyRequest(PeriodModifyRequest.builder()
                        .startDate(startDate)
                        .endDate(endDate)
                        .build())
                .participantStatusModifyRequest(ParticipantStatusModifyRequest.builder()
                        .currentHeadCount(currentHeadCount)
                        .maxHeadCount(maxHeadCount)
                        .build())
                .postModifyRequest(PostModifyRequest.builder()
                        .title(title)
                        .content(content)
                        .tags(tags)
                        .imageIds(imageIds)
                        .build())
                .build();
    }
}
