package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SuspendUserRequest {
    @NotNull
    private String reason;

    @NotNull
    @Future
    private LocalDateTime endDate;

    @AssertTrue(message = "정지 종료 시각의 최소 단위는 시간입니다. 분 이하의 단위가 포함되어서는 안됩니다.")
    private boolean isEndDateTruncated() {
        if (endDate == null) {
            return true;
        }
        return endDate.getMinute() == 0 && endDate.getSecond() == 0 && endDate.getNano() == 0;
    }
}
