package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostDetailInfo;
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

    private RecruitmentDetailInfo recruitment;

    private PostDetailInfo post;

}
