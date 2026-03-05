package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuspendUserRequest {
    @NotNull
    private String reason;

    @NotNull
    @Future
    private LocalDateTime endDate;
}
