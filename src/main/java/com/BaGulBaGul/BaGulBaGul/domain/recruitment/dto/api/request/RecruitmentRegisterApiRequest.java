package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.request;

import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.ParticipantStatusRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.PeriodRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request.PostRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request.RecruitmentRegisterRequest;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentRegisterApiRequest {

    @ApiModelProperty(value = "모집 인원")
    private Integer maxHeadCount;
    @ApiModelProperty(value = "현재 인원")
    private Integer currentHeadCount;

    @ApiModelProperty(value = "시작 시간")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "종료 시간")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "게시글 제목")
    private String title;

    @ApiModelProperty(value = "게시글 내용")
    private String content;

    @ApiModelProperty(value = "태그들", example = "[\"물놀이\",\"바베큐\"]")
    private List<String> tags;

    @ApiModelProperty(value = "등록한 이미지들의 resource id. 순서는 보존되며 첫번째 이미지가 대표이미지가 된다.")
    private List<Long> imageIds;

    public RecruitmentRegisterRequest toRecruitmentRegisterRequest() {
        return RecruitmentRegisterRequest.builder()
                .periodRegisterRequest(PeriodRegisterRequest.builder()
                        .startDate(startDate)
                        .endDate(endDate)
                        .build())
                .participantStatusRegisterRequest(ParticipantStatusRegisterRequest.builder()
                        .maxHeadCount(maxHeadCount)
                        .currentHeadCount(currentHeadCount)
                        .build())
                .postRegisterRequest(PostRegisterRequest.builder()
                        .title(title)
                        .content(content)
                        .tags(tags)
                        .imageIds(imageIds)
                        .build())
                .build();
    }
}
