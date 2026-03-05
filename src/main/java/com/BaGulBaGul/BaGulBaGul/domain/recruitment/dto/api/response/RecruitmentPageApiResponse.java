package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.PostSimpleApiInfo;
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
    private RecruitmentSimpleApiInfo recruitment;

    @ApiModelProperty(value = "게시글 정보")
    private PostSimpleApiInfo post;

    public static RecruitmentPageApiResponse from(RecruitmentSimpleResponse recruitmentSimpleResponse) {
        return RecruitmentPageApiResponse.builder()
                .recruitment(RecruitmentSimpleApiInfo.from(recruitmentSimpleResponse.getRecruitment()))
                .post(PostSimpleApiInfo.from(recruitmentSimpleResponse.getPost()))
                .build();
    }
}
