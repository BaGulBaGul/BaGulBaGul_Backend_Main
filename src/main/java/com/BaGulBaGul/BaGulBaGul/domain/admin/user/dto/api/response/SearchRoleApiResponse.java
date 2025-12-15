package com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.global.auth.constant.PermissionType;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.SearchRoleResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SearchRoleApiResponse {
    String roleName;
    List<PermissionType> permissions;
    public static SearchRoleApiResponse from(SearchRoleResponse searchRoleResponse) {
        return SearchRoleApiResponse.builder()
                .roleName(searchRoleResponse.getRoleName())
                .permissions(searchRoleResponse.getPermissions())
                .build();
    }
}
