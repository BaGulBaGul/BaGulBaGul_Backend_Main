package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.SearchRoleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.Role;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.RoleRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.SearchRoleRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    Page<SearchRoleResponse> findRoleByCondition(SearchRoleRequest request, Pageable pageable);
    Role createRole(RoleRegisterRequest roleRegisterRequest);
    void deleteRole(String roleName);
}
