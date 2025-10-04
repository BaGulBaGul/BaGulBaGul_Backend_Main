package com.BaGulBaGul.BaGulBaGul.domain.report.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportContentType;
import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportStatusState;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
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
public class ReportStatusDetailApiResponse {
    private ReportStatusApiResponse reportStatusInfo;

    @ApiModelProperty(value = "신고한 유저 이름 리스트. 신고 일시 오름차순 최대 5개")
    private List<String> reportingUserNames;
    @ApiModelProperty(value = "신고 일시 오름차순 최대 5개")
    private List<LocalDateTime> reportedAt;
}
