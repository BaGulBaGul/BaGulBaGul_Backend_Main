package com.BaGulBaGul.BaGulBaGul.domain.user.service;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.global.auth.Role;
import com.BaGulBaGul.BaGulBaGul.global.auth.sampledata.RoleSample;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.RoleService;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserRoleService_IntegrationTest {

    @Autowired
    UserJoinService userJoinService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    RoleService roleService;

    @Test
    @DisplayName("유저에 역할 추가")
    @Transactional
    void shouldOkWhenAddRole() {
        //given
        User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        Role role = roleService.createRole(RoleSample.getNormalRoleRegisterRequest());
        String roleName = role.getName();
        Long userId = user.getId();

        //when
        userRoleService.addRole(userId, roleName);

        //then
        assertTrue(userRoleService.hasRole(userId, roleName));
    }

    @Test
    @DisplayName("유저에 역할들 추가")
    @Transactional
    void shouldOkWhenAddRoles() {
        //given
        User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        Role role = roleService.createRole(RoleSample.getNormalRoleRegisterRequest());
        Role role2 = roleService.createRole(RoleSample.getNormal2RoleRegisterRequest());
        List<String> toAddRoles = List.of(role.getName(), role2.getName());

        Long userId = user.getId();

        //when
        userRoleService.addRoles(userId, toAddRoles);

        //then
        for(String roleName : toAddRoles) {
            assertTrue(userRoleService.hasRole(userId, roleName));
        }
    }

    @Test
    @DisplayName("유저에 역할 삭제")
    @Transactional
    void shouldOkWhenDeleteRole() {
        //given
        User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        Role role = roleService.createRole(RoleSample.getNormalRoleRegisterRequest());
        String roleName = role.getName();
        Long userId = user.getId();
        userRoleService.addRole(userId, roleName);

        //when
        userRoleService.deleteRole(userId, roleName);

        //then
        assertFalse(userRoleService.hasRole(userId, roleName));
    }

    @Test
    @DisplayName("유저에 역할들 삭제")
    @Transactional
    void shouldOkWhenDeleteRoles() {
        //given
        User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        Role role = roleService.createRole(RoleSample.getNormalRoleRegisterRequest());
        Role role2 = roleService.createRole(RoleSample.getNormal2RoleRegisterRequest());
        List<String> toAddRoles = List.of(role.getName(), role2.getName());

        Long userId = user.getId();
        userRoleService.addRoles(userId, toAddRoles);

        //when
        userRoleService.deleteRoles(userId, toAddRoles);

        //then
        for(String roleName : toAddRoles) {
            assertFalse(userRoleService.hasRole(userId, roleName));
        }
    }

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("유저의 역할을 변경")
    @Transactional
    void shouldOkWhenChangeRoles() {
        //given
        User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        Role role = roleService.createRole(RoleSample.getNormalRoleRegisterRequest());
        Role role2 = roleService.createRole(RoleSample.getNormal2RoleRegisterRequest());
        List<String> existingRoles = List.of(role.getName(), role2.getName());

        Long userId = user.getId();
        userRoleService.addRoles(userId, existingRoles);

        Role role3 = roleService.createRole(RoleSample.getNormal3RoleRegisterRequest());
        Role role4 = roleService.createRole(RoleSample.getNormal4RoleRegisterRequest());
        Role role5 = roleService.createRole(RoleSample.getNormal5RoleRegisterRequest());
        List<String> changeRoles = List.of(role3.getName(), role4.getName(), role5.getName());

        //when
        userRoleService.changeRole(userId, changeRoles);

        //then
        List<String> toDelete = existingRoles.stream()
                .filter(x -> !changeRoles.contains(x))
                .collect(Collectors.toList());
        for(String roleName : changeRoles) {
            assertTrue(userRoleService.hasRole(userId, roleName));
        }
        for(String roleName : toDelete) {
            assertFalse(userRoleService.hasRole(userId, roleName));
        }
    }
}
