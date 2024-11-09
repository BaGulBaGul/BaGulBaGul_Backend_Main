package com.BaGulBaGul.BaGulBaGul.domain.report.dto.api.request;

import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportType;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.ReportRegisterRequest;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public abstract class ReportRegisterApiRequest {
    @ApiModelProperty(value = "신고 종류", required = true)
    @NotNull(message = "신고 종류는 필수입니다")
    private ReportType reportType;

    @ApiModelProperty(value = "신고 메세지. 최대 1000자까지 가능", required = false)
    @Length(max = 1000, message = "신고 내용 길이는 최대 1000자입니다")
    private String message;

    public ReportRegisterRequest toReportRegisterAPIRequest(Long reportingUserId) {
        return ReportRegisterRequest.builder()
                .reportType(reportType)
                .message(message)
                .reportingUserId(reportingUserId)
                .build();
    }
}
