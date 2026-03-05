package com.BaGulBaGul.BaGulBaGul.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.BaGulBaGul.BaGulBaGul.domain.user.Role;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.PermissionType;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.RoleRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.SearchRoleResponse;

public class RoleTestUtils {
    public static void assertRoleRegister(Role role, RoleRegisterRequest roleRegisterRequest) {
        assertThat(role.getName()).isEqualTo(roleRegisterRequest.getRoleName());
    }

    public static void assertSearchRoleResponse(SearchRoleResponse searchRoleResponse, Role role) {
        assertThat(searchRoleResponse.getRoleName()).isEqualTo(role.getName());
        assertThat(searchRoleResponse.getPermissions())
                .containsExactlyInAnyOrder(role.getPermissions().toArray(PermissionType[]::new));
    }
}
