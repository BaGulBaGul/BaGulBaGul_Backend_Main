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
    @ApiModelProperty(value = "신고 종류", required = true, notes = "선택 가능한 종류 : NOT_RELEVANT, OFFENSIVE_CONTENT, DEFAMATORY, ETC")
    @NotNull(message = "신고 종류는 필수입니다")
    private ReportType reportType;

    @ApiModelProperty(value = "신고 메세지", required = false, notes = "최대 1000자까지 가능")
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
