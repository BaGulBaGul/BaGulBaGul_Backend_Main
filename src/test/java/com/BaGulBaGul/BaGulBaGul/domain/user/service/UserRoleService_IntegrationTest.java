package com.BaGulBaGul.BaGulBaGul.domain.user.service;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.domain.user.Role;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.RoleSample;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.GeneralRoleType;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.PermissionType;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    PermissionServiceImpl permissionService;

    User targetUser;
    User targetAdminUser;
    User requestUser;
    AuthenticatedUserInfo requestUserAuthInfo;
    User requestAdminUser;
    AuthenticatedUserInfo requestAdminUserAuthInfo;

    Role requestUserRole;
    Role role;
    Role role2;
    Role role3;
    Role role4;
    Role role5;

    @BeforeEach
    void init() {
        //역할 추가
        requestUserRole = roleService.createRole(RoleSample.getNormal6RoleRegisterRequest());
        role = roleService.createRole(RoleSample.getNormalRoleRegisterRequest());
        role2 = roleService.createRole(RoleSample.getNormal2RoleRegisterRequest());
        role3 = roleService.createRole(RoleSample.getNormal3RoleRegisterRequest());
        role4 = roleService.createRole(RoleSample.getNormal4RoleRegisterRequest());
        role5 = roleService.createRole(RoleSample.getNormal5RoleRegisterRequest());

        //요청자 역할에 유저 관리 권한 추가
        permissionService.addPermission(requestUserRole.getName(), PermissionType.MANAGE_USER);
        permissionService.refreshPermission();

        //대상 유저 추가
        targetUser = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        targetAdminUser = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());

        //요청자 유저 추가
        requestUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
        requestUserAuthInfo = new AuthenticatedUserInfo(requestUser.getId(), List.of(requestUserRole.getName()));
        requestAdminUser = userJoinService.registerUser(UserSample.getAdmin2UserRegisterRequest());
        requestAdminUserAuthInfo = new AuthenticatedUserInfo(requestAdminUser.getId(), List.of(GeneralRoleType.ADMIN.name()));
    }

    @Test
    @DisplayName("유저의 역할 추가")
    @Transactional
    void shouldOkWhenAddRole() {
        //given
        String roleName = role.getName();
        Long userId = targetUser.getId();

        //when
        userRoleService.addRole(userId, roleName);

        //then
        assertTrue(userRoleService.hasRole(userId, roleName));
    }

    @Test
    @DisplayName("요청자 정보를 검증 후 유저의 역할 추가")
    @Transactional
    void shouldOkWhenAddRoleWithAuth() {
        //given
        String roleName = role.getName();
        Long userId = targetUser.getId();

        //when
        userRoleService.addRole(requestUserAuthInfo, userId, roleName);

        //then
        assertTrue(userRoleService.hasRole(userId, roleName));
    }

    @Test
    @DisplayName("요청자 정보를 검증 후 유저의 역할 추가 - 요청자가 ADMIN이 아닌데 대상 유저가 ADMIN이라면 예외")
    @Transactional
    void shouldForbidden_WhenAddRoleWithAuthAndRequestUserIsNotAdminAndTargetUserIsAdmin() {
        assertThrows(GeneralException.class, () ->
                userRoleService.addRole(requestUserAuthInfo, targetAdminUser.getId(), role.getName()));
    }

    @Test
    @DisplayName("유저의 역할들 추가")
    @Transactional
    void shouldOkWhenAddRoles() {
        //given
        List<String> toAddRoles = List.of(role.getName(), role2.getName());
        Long userId = targetUser.getId();

        //when
        userRoleService.addRoles(userId, toAddRoles);

        //then
        for(String roleName : toAddRoles) {
            assertTrue(userRoleService.hasRole(userId, roleName));
        }
    }

    @Test
    @DisplayName("요청자 정보를 검증 후 유저의 역할들 추가")
    @Transactional
    void shouldOkWhenAddRolesWithAuth() {
        //given
        List<String> toAddRoles = List.of(role.getName(), role2.getName());
        Long userId = targetUser.getId();

        //when
        userRoleService.addRoles(requestUserAuthInfo, userId, toAddRoles);

        //then
        for(String roleName : toAddRoles) {
            assertTrue(userRoleService.hasRole(userId, roleName));
        }
    }

    @Test
    @DisplayName("요청자 정보를 검증 후 유저의 역할들 추가 - 요청자가 ADMIN이 아닌데 대상 유저가 ADMIN이라면 예외")
    @Transactional
    void shouldForbidden_WhenAddRolesWithAuthAndRequestUserIsNotAdminAndTargetUserIsAdmin() {
        assertThrows(GeneralException.class, () ->
                userRoleService.addRoles(requestUserAuthInfo, targetAdminUser.getId(), List.of(role.getName())));
    }

    @Test
    @DisplayName("유저의 역할 삭제")
    @Transactional
    void shouldOkWhenDeleteRole() {
        //given
        String roleName = role.getName();
        Long userId = targetUser.getId();
        userRoleService.addRole(userId, roleName);

        //when
        userRoleService.deleteRole(userId, roleName);

        //then
        assertFalse(userRoleService.hasRole(userId, roleName));
    }

    @Test
    @DisplayName("요청자 정보를 검증 후 유저의 역할 삭제")
    @Transactional
    void shouldOkWhenDeleteRoleWithAuth() {
        //given
        String roleName = role.getName();
        Long userId = targetUser.getId();
        userRoleService.addRole(userId, roleName);

        //when
        userRoleService.deleteRole(requestUserAuthInfo, userId, roleName);

        //then
        assertFalse(userRoleService.hasRole(userId, roleName));
    }

    @Test
    @DisplayName("요청자 정보를 검증 후 유저의 역할 삭제 - 요청자가 ADMIN이 아닌데 대상 유저가 ADMIN이라면 예외")
    @Transactional
    void shouldForbidden_WhenDeleteRoleWithAuthAndRequestUserIsNotAdminAndTargetUserIsAdmin() {
        //given
        Long userId = targetAdminUser.getId();
        String roleName = role.getName();
        userRoleService.addRole(userId, roleName);

        //when then
        assertThrows(GeneralException.class, () ->
                userRoleService.deleteRole(requestUserAuthInfo, userId, roleName));
    }


    @Test
    @DisplayName("유저의 역할들 삭제")
    @Transactional
    void shouldOkWhenDeleteRoles() {
        //given
        List<String> toAddRoles = List.of(role.getName(), role2.getName());

        Long userId = targetUser.getId();
        userRoleService.addRoles(userId, toAddRoles);

        //when
        userRoleService.deleteRoles(userId, toAddRoles);

        //then
        for(String roleName : toAddRoles) {
            assertFalse(userRoleService.hasRole(userId, roleName));
        }
    }

    @Test
    @DisplayName("요청자 정보를 검증 후 유저의 역할들 삭제")
    @Transactional
    void shouldOkWhenDeleteRolesWithAuth() {
        //given
        List<String> toAddRoles = List.of(role.getName(), role2.getName());

        Long userId = targetUser.getId();
        userRoleService.addRoles(userId, toAddRoles);

        //when
        userRoleService.deleteRoles(requestUserAuthInfo, userId, toAddRoles);

        //then
        for(String roleName : toAddRoles) {
            assertFalse(userRoleService.hasRole(userId, roleName));
        }
    }

    @Test
    @DisplayName("요청자 정보를 검증 후 유저의 역할들 삭제 - 요청자가 ADMIN이 아닌데 대상 유저가 ADMIN이라면 예외")
    @Transactional
    void shouldForbidden_WhenDeleteRolesWithAuthAndRequestUserIsNotAdminAndTargetUserIsAdmin() {
        //given
        List<String> toAddRoles = List.of(role.getName(), role2.getName());

        Long userId = targetAdminUser.getId();
        userRoleService.addRoles(userId, toAddRoles);

        //when then
        assertThrows(GeneralException.class, () ->
                userRoleService.deleteRoles(requestUserAuthInfo, userId, toAddRoles));
    }

    @Test
    @DisplayName("유저의 역할을 변경")
    @Transactional
    void shouldOkWhenChangeRoles() {
        //given
        List<String> existingRoles = List.of(role.getName(), role2.getName());
        Long userId = targetUser.getId();
        userRoleService.addRoles(userId, existingRoles);

        //when
        List<String> changeRoles = List.of(role3.getName(), role4.getName(), role5.getName());
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

    @Test
    @DisplayName("요청자 정보를 검증 후 유저의 역할을 변경")
    @Transactional
    void shouldOkWhenChangeRolesWithAuth() {
        //given
        List<String> existingRoles = List.of(role.getName(), role2.getName());
        Long userId = targetUser.getId();
        userRoleService.addRoles(userId, existingRoles);

        //when
        List<String> changeRoles = List.of(role3.getName(), role4.getName(), role5.getName());
        userRoleService.changeRole(requestUserAuthInfo, userId, changeRoles);

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

    @Test
    @DisplayName("요청자 정보를 검증 후 유저의 역할들 변경 - 요청자가 ADMIN이 아닌데 대상 유저가 ADMIN이라면 예외")
    @Transactional
    void shouldForbidden_WhenChangeRolesWithAuthAndRequestUserIsNotAdminAndTargetUserIsAdmin() {
        //given
        List<String> existingRoles = List.of(role.getName(), role2.getName());
        Long userId = targetAdminUser.getId();
        userRoleService.addRoles(userId, existingRoles);

        //when then
        List<String> changeRoles = List.of(role3.getName(), role4.getName(), role5.getName());
        assertThrows(GeneralException.class, () ->
                userRoleService.changeRole(requestUserAuthInfo, userId, changeRoles));
    }
}
