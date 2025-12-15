package com.BaGulBaGul.BaGulBaGul.global.auth.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.global.auth.Role;
import com.BaGulBaGul.BaGulBaGul.global.auth.RoleTestUtils;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.PermissionType;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.RoleRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.SearchRoleRequest;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.SearchRoleResponse;
import com.BaGulBaGul.BaGulBaGul.global.auth.repository.RoleRepository;
import com.BaGulBaGul.BaGulBaGul.global.auth.repository.querydsl.FindRoleByConditionImpl;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class RoleServiceImpl_IntegrationTest {

    @Autowired
    RoleService roleService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void init() {
        roleRepository.deleteAll();
    }

    @Test
    @DisplayName("조회 테스트")
    void findTest() {
        //given
        RoleRegisterRequest registerRequest = RoleRegisterRequest.builder()
                .roleName("testRole1")
                .build();
        Role role = roleService.createRole(registerRequest);
        entityManager.flush();
        entityManager.clear();
        //when
        Page<SearchRoleResponse> findResult = roleService.findRoleByCondition(SearchRoleRequest.builder().build(),
                Pageable.ofSize(10));
        //then
        assertThat(findResult.getTotalElements()).isEqualTo(1);
        List<SearchRoleResponse> content = findResult.getContent();
        RoleTestUtils.assertSearchRoleResponse(content.get(0), role);
    }

    @Test
    @DisplayName("생성 테스트")
    void createTest() {
        //when
        RoleRegisterRequest registerRequest = RoleRegisterRequest.builder()
                .roleName("testRole1")
                .build();
        Role role = roleService.createRole(registerRequest);
        //then
        RoleTestUtils.assertRoleRegister(role, registerRequest);
    }

    @Test
    @DisplayName("삭제 테스트")
    void deleteTest() {
        //given
        RoleRegisterRequest registerRequest = RoleRegisterRequest.builder()
                .roleName("testRole1")
                .build();
        Role role = roleService.createRole(registerRequest);
        entityManager.flush();
        entityManager.clear();
        //when
        roleService.deleteRole(role.getName());
        //then
        Optional<Role> findResult = roleRepository.findById(role.getName());
        assertThat(findResult.isEmpty()).isTrue();
    }
}
