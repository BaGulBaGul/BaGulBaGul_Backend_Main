package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RecruitmentRegisterResponse {
    @ApiModelProperty(value = "모잡글 id")
    private Long id;
}
