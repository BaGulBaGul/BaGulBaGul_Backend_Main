package com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.request;

import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportContentType;
import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportStatusState;
import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportType;
import java.util.List;
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
public class FindReportStatusByConditionRequest {
    /**
     * 게시물 타입 조건. 각각 or로 처리.
     */
    List<ReportContentType> reportContentTypes;
    /**
     * 처리 상태 조건
     */
    ReportStatusState reportStatusState;
}
