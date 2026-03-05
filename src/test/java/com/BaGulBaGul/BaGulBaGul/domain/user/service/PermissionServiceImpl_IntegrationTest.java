package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertThrows;

import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.domain.user.Role;
import com.BaGulBaGul.BaGulBaGul.domain.user.RolePermission;
import com.BaGulBaGul.BaGulBaGul.domain.user.RolePermission.RolePermissionId;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.GeneralRoleType;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.PermissionType;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.RoleRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.RolePermissionRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.RoleRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.RoleSample;
import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;


@ExtendWith(MockitoExtension.class)
@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PermissionServiceImpl_IntegrationTest {

    @Autowired
    PermissionServiceImpl permissionService;

    @Autowired
    RoleService roleService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RolePermissionRepository rolePermissionRepository;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    List<String> addedTestRoleNames;

    Role role1;
    Role role2;
    Role role3;

    @BeforeEach
    void init() {
        role1 = roleService.createRole(RoleSample.getNormalRoleRegisterRequest());
        role2 = roleService.createRole(RoleSample.getNormal2RoleRegisterRequest());
        role3 = roleService.createRole(RoleSample.getNormal3RoleRegisterRequest());

        addedTestRoleNames = List.of(role1.getName(), role2.getName(), role3.getName());
    }

    @AfterEach
    void tearDown() {
        //redis 초기화
        Set<String> keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);
        //db 정리
        //비동기와 after transaction을 함께 테스트하므로 테스트를 트랜젝션 내에서 실행하지 않고 따로 정리한다
        TransactionTemplate txTemplate = new TransactionTemplate(platformTransactionManager);
        txTemplate.executeWithoutResult(new Consumer<TransactionStatus>() {
            @Override
            public void accept(TransactionStatus transactionStatus) {
                for(String roleName : addedTestRoleNames) {
                    roleRepository.deleteById(roleName);
                }
            }
        });
        //권한 캐시 초기화
        permissionService.refreshPermission();
    }

    @Test
    @DisplayName("여러 역할의 권한을 확인")
    void checkMultipleRolePermissionTest() {
        //given
        PermissionType permissionType1 = PermissionType.MANAGE_EVENT;
        permissionService.addPermission(role1.getName(), permissionType1);

        PermissionType permissionType2 = PermissionType.MANAGE_USER;
        permissionService.addPermission(role1.getName(), permissionType2);

        //when
        List<String> roleNames = List.of(role1.getName(), role2.getName(), role3.getName());
        await().atMost(20, TimeUnit.SECONDS)
                .pollInterval(200, TimeUnit.MILLISECONDS)
                .until(() -> {

                    if(permissionService.checkPermission(roleNames, permissionType1) &&
                            permissionService.checkPermission(roleNames, permissionType2)
                    ) {
                        return true;
                    }
                    return false;
                });

    }

    @Test
    @DisplayName("역할에 권한을 추가")
    void addTest() {
        //given
        //when
        PermissionType permissionType = PermissionType.MANAGE_EVENT;
        permissionService.addPermission(role1.getName(), permissionType);

        //then
        await().atMost(100, TimeUnit.SECONDS)
                .pollInterval(200, TimeUnit.MILLISECONDS)
                .until(() -> {
                    if(permissionService.checkPermission(role1.getName(), permissionType)) {
                        return true;
                    }
                    return false;
                });

        RolePermission rolePermission = rolePermissionRepository.findById(
                new RolePermissionId(role1.getName(), permissionType.name())).orElse(null);
        assertThat(rolePermission).isNotNull();
    }

    @Test
    @DisplayName("역할에 권한을 추가 - ADMIN 역할 변경시 예외")
    void shouldForbidden_WhenAddPermissionAndTargetRoleIsAdmin() {
        assertThrows(GeneralException.class, () -> {
            permissionService.addPermission(GeneralRoleType.ADMIN.name(), PermissionType.MANAGE_EVENT);
        });
    }

    @Test
    @DisplayName("역할의 권한을 중복되게 추가")
    void addTestWhenDuplicate() {
        //given
        PermissionType permissionType = PermissionType.MANAGE_EVENT;
        permissionService.addPermission(role1.getName(), permissionType);

        //when
        permissionService.addPermission(role1.getName(), permissionType);

        //then
        await().atMost(100, TimeUnit.SECONDS)
                .pollInterval(200, TimeUnit.MILLISECONDS)
                .until(() -> {
                    if(permissionService.checkPermission(role1.getName(), permissionType)) {
                        return true;
                    }
                    return false;
                });

        RolePermission rolePermission = rolePermissionRepository.findById(
                new RolePermissionId(role1.getName(), permissionType.name())).orElse(null);
        assertThat(rolePermission).isNotNull();
    }

    @Test
    @DisplayName("역할의 권한을 삭제")
    void deleteTest() {
        //given
        PermissionType permissionType = PermissionType.MANAGE_EVENT;
        permissionService.addPermission(role1.getName(), permissionType);

        //when
        permissionService.deletePermission(role1.getName(), permissionType);

        //then
        await().atMost(100, TimeUnit.SECONDS)
                .pollInterval(200, TimeUnit.MILLISECONDS)
                .until(() -> {
                    if(permissionService.checkPermission(role1.getName(), permissionType)) {
                        return false;
                    }
                    return true;
                });

        RolePermission rolePermission = rolePermissionRepository.findById(
                new RolePermissionId(role1.getName(), permissionType.name())).orElse(null);
        assertThat(rolePermission).isNull();
    }

    @Test
    @DisplayName("역할의 권한을 삭제 - ADMIN 역할 변경시 예외")
    void shouldForbidden_WhenDeletePermissionAndTargetRoleIsAdmin() {
        assertThrows(GeneralException.class, () -> {
            permissionService.deletePermission(GeneralRoleType.ADMIN.name(), PermissionType.MANAGE_EVENT);
        });
    }

    @Test
    @DisplayName("역할의 권한을 없는데 삭제")
    void deleteWhenNotExistTest() {
        //given
        PermissionType permissionType = PermissionType.MANAGE_EVENT;

        //when
        permissionService.deletePermission(role1.getName(), permissionType);

        //then
        await().atMost(100, TimeUnit.SECONDS)
                .pollInterval(200, TimeUnit.MILLISECONDS)
                .until(() -> {
                    if(permissionService.checkPermission(role1.getName(), permissionType)) {
                        return false;
                    }
                    return true;
                });

        RolePermission rolePermission = rolePermissionRepository.findById(
                new RolePermissionId(role1.getName(), permissionType.name())).orElse(null);
        assertThat(rolePermission).isNull();
    }


    @Test
    @DisplayName("역할의 권한을 변경")
    void changeTest() {
        //given
        Set<PermissionType> basePermissions = Set.of(PermissionType.MANAGE_EVENT, PermissionType.MANAGE_RECRUITMENT);
        permissionService.addPermissions(role1.getName(), basePermissions);

        //when
        Set<PermissionType> newPermissions = Set.of(PermissionType.MANAGE_EVENT, PermissionType.MANAGE_USER);
        permissionService.changePermission(role1.getName(),
                newPermissions);

        //then
        Set<PermissionType> deletedPermissions = basePermissions.stream()
                .filter(permissionType -> !newPermissions.contains(permissionType))
                .collect(Collectors.toSet());
        await().atMost(10000, TimeUnit.SECONDS)
                .pollInterval(200, TimeUnit.MILLISECONDS)
                .until(() -> {
                    for(PermissionType permissionType : newPermissions) {
                        if(!permissionService.checkPermission(role1.getName(), permissionType)) {
                            return false;
                        }
                    }
                    for(PermissionType permissionType : deletedPermissions) {
                        if(permissionService.checkPermission(role1.getName(), permissionType)) {
                            return false;
                        }
                    }
                    return true;
                });
    }

    @Test
    @DisplayName("역할의 권한을 변경 - ADMIN 역할 변경시 예외")
    void shouldForbidden_WhenChangePermissionAndTargetRoleIsAdmin() {
        Set<PermissionType> newPermissions = Set.of(PermissionType.MANAGE_EVENT, PermissionType.MANAGE_USER);
        assertThrows(GeneralException.class, () -> {
            permissionService.changePermission(GeneralRoleType.ADMIN.name(), newPermissions);
        });
    }
}