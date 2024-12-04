package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostDetailInfo;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentDetailInfo;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentDetailResponse;
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
    private RecruitmentDetailInfo recruitment;

    @ApiModelProperty(value = "게시글 정보")
    private PostDetailInfo post;

    public static RecruitmentDetailApiResponse from(RecruitmentDetailResponse recruitmentDetailResponse) {
        return RecruitmentDetailApiResponse.builder()
                .recruitment(recruitmentDetailResponse.getRecruitment())
                .post(recruitmentDetailResponse.getPost())
                .build();
    }
}
