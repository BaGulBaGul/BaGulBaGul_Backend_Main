package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostDetailInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class RecruitmentDetailResponse {

    @ApiModelProperty(value = "모잡글 정보")
    private RecruitmentDetailInfo recruitment;

    @ApiModelProperty(value = "게시글 정보")
    private PostDetailInfo post;

}
