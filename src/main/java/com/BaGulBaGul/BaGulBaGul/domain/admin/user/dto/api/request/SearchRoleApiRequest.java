package com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request;

import com.BaGulBaGul.BaGulBaGul.global.auth.constant.PermissionType;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.SearchRoleRequest;
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
public class SearchRoleApiRequest {
    String roleName;
    List<PermissionType> permissions;

    public SearchRoleRequest toSearchRoleRequest() {
        return SearchRoleRequest.builder()
                .roleName(roleName)
                .permissions(permissions)
                .build();
    }
}
