package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RoleRegisterRequest {
    String roleName;
}
