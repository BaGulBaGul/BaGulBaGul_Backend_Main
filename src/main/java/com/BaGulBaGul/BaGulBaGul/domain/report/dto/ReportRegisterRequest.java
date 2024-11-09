package com.BaGulBaGul.BaGulBaGul.domain.report.dto;

import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportType;
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
public class ReportRegisterRequest {
    private ReportType reportType;
    private String message;
    private Long reportingUserId;
}
