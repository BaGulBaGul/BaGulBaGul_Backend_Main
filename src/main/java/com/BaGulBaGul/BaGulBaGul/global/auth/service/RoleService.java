package com.BaGulBaGul.BaGulBaGul.global.auth.service;

import com.BaGulBaGul.BaGulBaGul.global.auth.Role;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.RoleRegisterRequest;

public interface RoleService {
    Role createRole(RoleRegisterRequest roleRegisterRequest);
    void deleteRole(String roleName);
}
