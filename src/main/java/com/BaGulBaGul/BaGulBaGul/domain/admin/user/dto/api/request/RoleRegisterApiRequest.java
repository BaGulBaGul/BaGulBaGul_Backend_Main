package com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.RoleRegisterRequest;
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
public class RoleRegisterApiRequest {
    String roleName;
    public RoleRegisterRequest toRoleRegisterRequest() {
        return RoleRegisterRequest.builder()
                .roleName(roleName)
                .build();
    }
}
