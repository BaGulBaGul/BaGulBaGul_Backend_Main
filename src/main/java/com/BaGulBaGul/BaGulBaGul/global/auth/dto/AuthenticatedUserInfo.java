package com.BaGulBaGul.BaGulBaGul.global.auth.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticatedUserInfo {
    Long userId;
    List<String> roles;
}
