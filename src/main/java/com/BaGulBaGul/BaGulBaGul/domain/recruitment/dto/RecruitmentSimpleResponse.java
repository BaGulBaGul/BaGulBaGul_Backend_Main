package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostSimpleInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class RecruitmentSimpleResponse {

    @ApiModelProperty(value = "모잡글 정보")
    private RecruitmentSimpleInfo recruitment;

    @ApiModelProperty(value = "게시글 정보")
    private PostSimpleInfo post;

}
