package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostSimpleInfo;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response.RecruitmentSimpleInfo;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response.RecruitmentSimpleResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class RecruitmentPageApiResponse {

    @ApiModelProperty(value = "모잡글 정보")
    private RecruitmentSimpleInfo recruitment;

    @ApiModelProperty(value = "게시글 정보")
    private PostSimpleInfo post;

    public static RecruitmentPageApiResponse from(RecruitmentSimpleResponse recruitmentSimpleResponse) {
        return RecruitmentPageApiResponse.builder()
                .recruitment(recruitmentSimpleResponse.getRecruitment())
                .post(recruitmentSimpleResponse.getPost())
                .build();
    }
}
