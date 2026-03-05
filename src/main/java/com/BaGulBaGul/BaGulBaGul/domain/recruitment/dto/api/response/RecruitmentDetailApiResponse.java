package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.PostDetailApiInfo;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostDetailInfo;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response.RecruitmentDetailInfo;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response.RecruitmentDetailResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class RecruitmentDetailApiResponse {

    @ApiModelProperty(value = "모잡글 정보")
    private RecruitmentDetailApiInfo recruitment;

    @ApiModelProperty(value = "게시글 정보")
    private PostDetailApiInfo post;

    public static RecruitmentDetailApiResponse from(RecruitmentDetailResponse recruitmentDetailResponse) {
        return RecruitmentDetailApiResponse.builder()
                .recruitment(RecruitmentDetailApiInfo.from(recruitmentDetailResponse.getRecruitment()))
                .post(PostDetailApiInfo.from(recruitmentDetailResponse.getPost()))
                .build();
    }
}
