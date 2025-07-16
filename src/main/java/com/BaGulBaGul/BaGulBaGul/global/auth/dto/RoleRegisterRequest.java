package com.BaGulBaGul.BaGulBaGul.global.auth.dto;


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
