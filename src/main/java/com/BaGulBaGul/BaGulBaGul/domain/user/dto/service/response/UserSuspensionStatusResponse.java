package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSuspensionStatusResponse {
    boolean isSuspended;
    LocalDateTime endDate;
    String reason;
}
