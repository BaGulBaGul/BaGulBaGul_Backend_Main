package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordLoginUserRegisterRequest {
    String loginId;
    String loginPassword;
}
