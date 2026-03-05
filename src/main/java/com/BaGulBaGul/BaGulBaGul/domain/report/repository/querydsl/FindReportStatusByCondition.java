package com.BaGulBaGul.BaGulBaGul.domain.report.repository.querydsl;

import com.BaGulBaGul.BaGulBaGul.domain.report.ReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.request.FindReportStatusByConditionRequest;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

public interface FindReportStatusByCondition {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    class ReportStatusIdsWithTotalCountOfPageResult {
        private List<ReportStatus> reportStatuses;
        private Long totalCount;
    }

    ReportStatusIdsWithTotalCountOfPageResult findReportStatusIdsByConditionAndPageable(
        FindReportStatusByConditionRequest conditionRequest, Pageable pageable
    );
}
