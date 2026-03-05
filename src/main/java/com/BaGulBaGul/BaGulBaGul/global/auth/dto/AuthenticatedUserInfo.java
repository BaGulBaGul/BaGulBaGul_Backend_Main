package com.BaGulBaGul.BaGulBaGul.global.auth.dto;

import com.BaGulBaGul.BaGulBaGul.global.auth.constant.GeneralRoleType;
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

    public boolean hasRole(String roleName) {
        return roles.contains(roleName);
    }

    public boolean hasRole(GeneralRoleType roleType) {
        return hasRole(roleType.name());
    }
}
