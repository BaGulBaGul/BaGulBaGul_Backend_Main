package com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.global.auth.constant.PermissionType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SearchPermissionApiResponse {
    PermissionType[] permissions;
}
