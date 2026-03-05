package com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request;

import java.util.Set;
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
public class SetUserRoleApiRequest {
    Set<String> roles;
}
