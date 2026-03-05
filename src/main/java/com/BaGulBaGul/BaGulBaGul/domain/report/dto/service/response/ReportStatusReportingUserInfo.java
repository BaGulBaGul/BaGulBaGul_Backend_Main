package com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.response;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.UserInfoResponse;
import java.time.LocalDateTime;
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
public class ReportStatusReportingUserInfo {
    UserInfoResponse reportingUserInfo;
    LocalDateTime reportedAt;
}
