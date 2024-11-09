package com.BaGulBaGul.BaGulBaGul.domain.report.dto.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportRegisterApiResponse {
    @ApiModelProperty(value = "신고 id")
    private Long reportId;
}
