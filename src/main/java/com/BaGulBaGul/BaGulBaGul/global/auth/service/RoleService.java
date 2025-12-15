package com.BaGulBaGul.BaGulBaGul.global.auth.service;

import com.BaGulBaGul.BaGulBaGul.global.auth.dto.SearchRoleResponse;
import com.BaGulBaGul.BaGulBaGul.global.auth.Role;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.RoleRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.SearchRoleRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    Page<SearchRoleResponse> findRoleByCondition(SearchRoleRequest request, Pageable pageable);
    Role createRole(RoleRegisterRequest roleRegisterRequest);
    void deleteRole(String roleName);
}
