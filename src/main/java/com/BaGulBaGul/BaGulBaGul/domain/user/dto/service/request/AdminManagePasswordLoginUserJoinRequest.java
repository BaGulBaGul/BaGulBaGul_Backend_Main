package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request;

import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class AdminManagePasswordLoginUserJoinRequest {
    @Valid
    UserRegisterRequest userRegisterRequest;
    @Valid
    PasswordLoginUserRegisterRequest passwordLoginUserRegisterRequest;
}
