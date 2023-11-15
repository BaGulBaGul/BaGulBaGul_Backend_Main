package com.BaGulBaGul.BaGulBaGul.domain.user.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class UserRegisterRequest {
    String nickname;
    String email;
}
