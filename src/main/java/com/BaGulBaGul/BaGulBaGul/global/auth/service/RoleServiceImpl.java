package com.BaGulBaGul.BaGulBaGul.global.auth.service;

import com.BaGulBaGul.BaGulBaGul.global.auth.Role;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.RoleRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.global.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

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
}
