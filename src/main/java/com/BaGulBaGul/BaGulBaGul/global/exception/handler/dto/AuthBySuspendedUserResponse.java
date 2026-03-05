package com.BaGulBaGul.BaGulBaGul.global.exception.handler.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthBySuspendedUserResponse {
    LocalDateTime endDate;
    String reason;
}
