package com.BaGulBaGul.BaGulBaGul.global.auth.dto;

import com.BaGulBaGul.BaGulBaGul.global.auth.Permission;
import com.BaGulBaGul.BaGulBaGul.global.auth.Role;
import com.BaGulBaGul.BaGulBaGul.global.auth.RolePermission;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.PermissionType;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SearchRoleResponse {
    String roleName;
    List<PermissionType> permissions;

    public static SearchRoleResponse from(Role role) {
        return SearchRoleResponse.builder()
                .roleName(role.getName())
                .permissions(role.getRolePermissions().stream()
                        .map(RolePermission::getPermission)
                        .map(Permission::getPermissionType)
                        .collect(Collectors.toList()))
                .build();
    }
}
