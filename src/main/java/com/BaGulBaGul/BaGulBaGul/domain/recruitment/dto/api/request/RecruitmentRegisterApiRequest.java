package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.request;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentRegisterRequest;
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
    @Min(value = 1, message = "모집 인원은 {1}명 이상이여야 합니다")
    private Integer maxHeadCount;

    @ApiModelProperty(value = "시작 시간")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "종료 시간")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "게시글 제목")
    @NotBlank(message = "제목은 null이거나 빈 문자열일 수 없습니다.")
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

    public RecruitmentRegisterRequest toRecruitmentRegisterRequest() {
        throw new UnsupportedOperationException();
    }
}
