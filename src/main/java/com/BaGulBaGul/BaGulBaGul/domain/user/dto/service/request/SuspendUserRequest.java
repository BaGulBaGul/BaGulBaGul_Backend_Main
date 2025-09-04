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

    @AssertTrue(message = "endDate must be truncated to the hour")
    private boolean isEndDateTruncated() {
        if (endDate == null) {
            return true;
        }
        return endDate.getMinute() == 0 && endDate.getSecond() == 0 && endDate.getNano() == 0;
    }
}
