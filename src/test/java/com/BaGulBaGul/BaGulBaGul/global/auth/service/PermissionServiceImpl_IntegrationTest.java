package com.BaGulBaGul.BaGulBaGul.global.auth.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;

import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.global.auth.Role;
import com.BaGulBaGul.BaGulBaGul.global.auth.RolePermission;
import com.BaGulBaGul.BaGulBaGul.global.auth.RolePermission.RolePermissionId;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.PermissionType;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.RoleRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.global.auth.repository.RolePermissionRepository;
import com.BaGulBaGul.BaGulBaGul.global.auth.repository.RoleRepository;
import com.BaGulBaGul.BaGulBaGul.global.auth.sampledata.RoleSample;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import org.springframework.transaction.support.DefaultTransactionDefinition;
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

    List<String> addedTestRoleNames = new ArrayList<>();

    @AfterEach
    void tearDown() {
        Set<String> keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);
        TransactionTemplate txTemplate = new TransactionTemplate(platformTransactionManager);
        txTemplate.executeWithoutResult(new Consumer<TransactionStatus>() {
            @Override
            public void accept(TransactionStatus transactionStatus) {
                for(String roleName : addedTestRoleNames) {
                    roleRepository.deleteById(roleName);
                }
                addedTestRoleNames.clear();
            }
        });
    }

    @Test
    @DisplayName("역할의 권한을 추가")
    void addTest() {
        //given
        String roleName = createRole(RoleSample.getNormalRoleRegisterRequest());

        PermissionType permissionType = PermissionType.MANAGE_EVENT;
        permissionService.addPermission(roleName, permissionType);

        //when
        permissionService.addPermission(roleName, permissionType);

        //then
        await().atMost(100, TimeUnit.SECONDS)
                .pollInterval(200, TimeUnit.MILLISECONDS)
                .until(() -> {
                    if(permissionService.checkPermission(roleName, permissionType)) {
                        return true;
                    }
                    return false;
                });

        RolePermission rolePermission = rolePermissionRepository.findById(
                new RolePermissionId(roleName, permissionType.name())).orElse(null);
        assertThat(rolePermission).isNotNull();
    }

    @Test
    @DisplayName("역할의 권한을 중복되게 추가")
    void addTestWhenDuplicate() {
        //given
        String roleName = createRole(RoleSample.getNormalRoleRegisterRequest());

        PermissionType permissionType = PermissionType.MANAGE_EVENT;
        permissionService.addPermission(roleName, permissionType);

        //when
        permissionService.addPermission(roleName, permissionType);

        //then
        await().atMost(100, TimeUnit.SECONDS)
                .pollInterval(200, TimeUnit.MILLISECONDS)
                .until(() -> {
                    if(permissionService.checkPermission(roleName, permissionType)) {
                        return true;
                    }
                    return false;
                });

        RolePermission rolePermission = rolePermissionRepository.findById(
                new RolePermissionId(roleName, permissionType.name())).orElse(null);
        assertThat(rolePermission).isNotNull();
    }

    @Test
    @DisplayName("역할의 권한을 삭제")
    void deleteTest() {
        //given
        String roleName = createRole(RoleSample.getNormalRoleRegisterRequest());

        PermissionType permissionType = PermissionType.MANAGE_EVENT;
        permissionService.addPermission(roleName, permissionType);

        //when
        permissionService.deletePermission(roleName, permissionType);

        //then
        await().atMost(100, TimeUnit.SECONDS)
                .pollInterval(200, TimeUnit.MILLISECONDS)
                .until(() -> {
                    if(permissionService.checkPermission(roleName, permissionType)) {
                        return false;
                    }
                    return true;
                });

        RolePermission rolePermission = rolePermissionRepository.findById(
                new RolePermissionId(roleName, permissionType.name())).orElse(null);
        assertThat(rolePermission).isNull();
    }

    @Test
    @DisplayName("역할의 권한을 없는데 삭제")
    void deleteWhenNotExistTest() {
        //given
        String roleName = createRole(RoleSample.getNormalRoleRegisterRequest());

        PermissionType permissionType = PermissionType.MANAGE_EVENT;
        permissionService.addPermission(roleName, permissionType);

        //when
        permissionService.deletePermission(roleName, permissionType);

        //then
        await().atMost(100, TimeUnit.SECONDS)
                .pollInterval(200, TimeUnit.MILLISECONDS)
                .until(() -> {
                    if(permissionService.checkPermission(roleName, permissionType)) {
                        return false;
                    }
                    return true;
                });

        RolePermission rolePermission = rolePermissionRepository.findById(
                new RolePermissionId(roleName, permissionType.name())).orElse(null);
        assertThat(rolePermission).isNull();
    }


    @Test
    @DisplayName("역할의 권한을 변경")
    void changeTest() {
        //given
        String roleName = createRole(RoleSample.getNormalRoleRegisterRequest());

        Set<PermissionType> basePermissions = Set.of(PermissionType.MANAGE_EVENT, PermissionType.MANAGE_RECRUITMENT);
        permissionService.addPermissions(roleName, basePermissions);

        //when
        Set<PermissionType> newPermissions = Set.of(PermissionType.MANAGE_EVENT, PermissionType.MANAGE_USER);
        permissionService.changePermission(roleName,
                newPermissions);

        //then
        Set<PermissionType> deletedPermissions = basePermissions.stream()
                .filter(permissionType -> !newPermissions.contains(permissionType))
                .collect(Collectors.toSet());
        await().atMost(10000, TimeUnit.SECONDS)
                .pollInterval(200, TimeUnit.MILLISECONDS)
                .until(() -> {
                    for(PermissionType permissionType : newPermissions) {
                        if(!permissionService.checkPermission(roleName, permissionType)) {
                            return false;
                        }
                    }
                    for(PermissionType permissionType : deletedPermissions) {
                        if(permissionService.checkPermission(roleName, permissionType)) {
                            return false;
                        }
                    }
                    return true;
                });
    }

    private String createRole(RoleRegisterRequest roleRegisterRequest) {
        Role role = roleService.createRole(roleRegisterRequest);
        String roleName = role.getName();
        addedTestRoleNames.add(roleName);
        return roleName;
    }
}