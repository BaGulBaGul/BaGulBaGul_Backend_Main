package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class SocialLoginUserJoinRequest {
    @NotBlank
    String joinToken;

    @Valid
    UserRegisterRequest userRegisterRequest;
}
