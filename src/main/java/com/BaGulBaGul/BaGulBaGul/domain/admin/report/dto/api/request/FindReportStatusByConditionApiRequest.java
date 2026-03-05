package com.BaGulBaGul.BaGulBaGul.domain.admin.report.dto.api.request;

import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportContentType;
import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportStatusState;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.request.FindReportStatusByConditionRequest;
import io.swagger.annotations.ApiModelProperty;
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
public class FindReportStatusByConditionApiRequest {
    @ApiModelProperty(value = "게시물 타입 조건. 각각 or로 처리.")
    List<ReportContentType> reportContentTypes;

    @ApiModelProperty(value = "처리 상태 조건")
    ReportStatusState reportStatusState;

    public FindReportStatusByConditionRequest toServiceRequest() {
        return FindReportStatusByConditionRequest.builder()
                .reportContentTypes(reportContentTypes)
                .reportStatusState(reportStatusState)
                .build();
    }
}
