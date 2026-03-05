package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LiftUserSuspensionRequest {
    @NotNull
    private String reason;
}
