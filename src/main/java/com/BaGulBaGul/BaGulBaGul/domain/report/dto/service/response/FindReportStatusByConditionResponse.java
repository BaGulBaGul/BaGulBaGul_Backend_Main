package com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.response;

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
public class FindReportStatusByConditionResponse {

    private ReportStatusInfo reportStatusInfo;
    private ReportStatusOriginalContentInfo reportStatusOriginalContentInfo;
}
