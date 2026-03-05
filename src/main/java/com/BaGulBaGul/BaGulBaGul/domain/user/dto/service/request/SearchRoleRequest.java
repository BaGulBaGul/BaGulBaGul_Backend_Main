package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request;

import com.BaGulBaGul.BaGulBaGul.global.auth.constant.PermissionType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SearchRoleRequest {
    String roleName;
    List<PermissionType> permissions;
}
