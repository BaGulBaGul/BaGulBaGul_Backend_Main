package com.BaGulBaGul.BaGulBaGul.global.auth.exception;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccountSuspendedException extends RuntimeException {
    private LocalDateTime endDate;
    private String reason;
}
