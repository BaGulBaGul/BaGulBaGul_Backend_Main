package com.BaGulBaGul.BaGulBaGul.global.auth.service;

import com.BaGulBaGul.BaGulBaGul.global.auth.dto.SearchRoleResponse;
import com.BaGulBaGul.BaGulBaGul.global.auth.Role;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.RoleRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.SearchRoleRequest;
import com.BaGulBaGul.BaGulBaGul.global.auth.repository.RoleRepository;
import com.BaGulBaGul.BaGulBaGul.global.auth.repository.querydsl.FindRoleByCondition.RoleNamesWithTotalCount;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public Page<SearchRoleResponse> findRoleByCondition(SearchRoleRequest request, Pageable pageable) {
        RoleNamesWithTotalCount pageResult = roleRepository.findRoleNamesByCondition(
                request, pageable);
        List<SearchRoleResponse> searchRoleResponses = getRoleSearchResponses(pageResult.getRoleNames());
        return new PageImpl<>(searchRoleResponses, pageable, pageResult.getTotalCount());
    }

    @Override
    public Role createRole(RoleRegisterRequest roleRegisterRequest) {
        Role role = roleRepository.save(
                Role.builder()
                        .name(roleRegisterRequest.getRoleName())
                        .build()
        );
        return role;
    }

    @Override
    public void deleteRole(String roleName) {
        //연결된 권한은 cascade로 삭제
        roleRepository.deleteById(roleName);
    }

    private List<SearchRoleResponse> getRoleSearchResponses(List<String> roleNames) {
        List<Role> roles = roleRepository.findByIdsWithPermissions(roleNames);
        return roles.stream().map(SearchRoleResponse::from).collect(Collectors.toList());
    }
}
