package com.BaGulBaGul.BaGulBaGul.domain.common.dto.request;

import java.time.LocalDateTime;
import javax.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeriodRegisterRequest {
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @AssertTrue(message = "시작 날짜는 종료 날짜보다 빨라야 합니다.")
    private boolean isStartDateBeforeEndDate() {
        return startDate == null || endDate == null || startDate.isBefore(endDate);
    }
}
