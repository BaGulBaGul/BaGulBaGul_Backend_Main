package com.BaGulBaGul.BaGulBaGul.global.auth.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.global.auth.Role;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.PermissionType;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.RoleRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.SearchRoleRequest;
import com.BaGulBaGul.BaGulBaGul.global.auth.repository.querydsl.FindRoleByCondition.RoleNamesWithTotalCount;
import com.BaGulBaGul.BaGulBaGul.global.auth.repository.querydsl.FindRoleByConditionImpl;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.PermissionService;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.RoleService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class FindRoleByConditionImpl_IntegrationTest {
    @Autowired
    FindRoleByConditionImpl findRoleByCondition;

    @Autowired
    RoleService roleService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PermissionService permissionService;

    Role testRole1;
    Role testRole2;
    Role test3Role;

    @BeforeEach
    void init() {
        roleRepository.deleteAll();
        testRole1 = roleService.createRole(RoleRegisterRequest.builder()
                .roleName("testRole1")
                .build()
        );
        testRole2 = roleService.createRole(RoleRegisterRequest.builder()
                .roleName("testRole2")
                .build()
        );
        test3Role = roleService.createRole(RoleRegisterRequest.builder()
                .roleName("test3Role")
                .build()
        );
        permissionService.addPermissions(testRole1.getName(), List.of(PermissionType.MANAGE_ROLE));
        permissionService.addPermissions(testRole2.getName(), List.of(PermissionType.MANAGE_USER));
        permissionService.addPermissions(test3Role.getName(),
                List.of(PermissionType.MANAGE_USER, PermissionType.MANAGE_EVENT));
    }

    @Test
    @DisplayName("조건 없이 검색")
    void findAll() {
        //when
        SearchRoleRequest searchRequest = SearchRoleRequest.builder()
                .build();
        RoleNamesWithTotalCount result = findRoleByCondition.findRoleNamesByCondition(searchRequest,
                Pageable.ofSize(10));
        //then
        List<String> expected = List.of(testRole1.getName(), testRole2.getName(), test3Role.getName());
        assertThat(result.getTotalCount()).isEqualTo(expected.size());
        assertThat(result.getRoleNames()).containsExactlyInAnyOrder(expected.toArray(String[]::new));
    }

    @Test
    @DisplayName("역할 이름이 문자열을 포함하는 경우")
    void findByRoleName() {
        //when
        SearchRoleRequest searchRequest = SearchRoleRequest.builder()
                .roleName("testRole")
                .build();
        RoleNamesWithTotalCount result = findRoleByCondition.findRoleNamesByCondition(searchRequest,
                Pageable.ofSize(10));
        //then
        List<String> expected = List.of(testRole1.getName(), testRole2.getName());
        assertThat(result.getTotalCount()).isEqualTo(expected.size());
        assertThat(result.getRoleNames()).containsExactlyInAnyOrder(expected.toArray(String[]::new));
    }

    @Test
    @DisplayName("특정 권한을 가진 역할 검색 - 단일 권한")
    void findBySinglePermission() {
        //when
        SearchRoleRequest searchRequest = SearchRoleRequest.builder()
                .permissions(List.of(PermissionType.MANAGE_USER))
                .build();
        RoleNamesWithTotalCount result = findRoleByCondition.findRoleNamesByCondition(searchRequest,
                Pageable.ofSize(10));
        //then
        List<String> expected = List.of(testRole2.getName(), test3Role.getName());
        assertThat(result.getTotalCount()).isEqualTo(expected.size());
        assertThat(result.getRoleNames()).containsExactlyInAnyOrder(expected.toArray(String[]::new));
    }

    @Test
    @DisplayName("특정 권한을 가진 역할 검색 - 복수 권한")
    void findByMultiplePermission() {
        //when
        SearchRoleRequest searchRequest = SearchRoleRequest.builder()
                .permissions(List.of(PermissionType.MANAGE_USER, PermissionType.MANAGE_EVENT))
                .build();
        RoleNamesWithTotalCount result = findRoleByCondition.findRoleNamesByCondition(searchRequest,
                Pageable.ofSize(10));
        //then
        List<String> expected = List.of(test3Role.getName());
        assertThat(result.getTotalCount()).isEqualTo(expected.size());
        assertThat(result.getRoleNames()).containsExactlyInAnyOrder(expected.toArray(String[]::new));
    }
}
