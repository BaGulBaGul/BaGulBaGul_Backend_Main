package com.BaGulBaGul.BaGulBaGul.global.auth.service;

import com.BaGulBaGul.BaGulBaGul.global.auth.constant.PermissionType;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface PermissionService {

    /**
     * 어떤 역할이 어떤 권한을 가지고 있는지 확인
     */
    boolean checkPermission(String roleName, PermissionType permissionType);

    /**
     * 어떤 역할이 어떤 권한을 가지고 있는지 확인
     */
    boolean checkPermission(Collection<String> roleNames, PermissionType permissionType);

    /**
     * 어떤 역할에 어떤 권한을 추가
     */
    void addPermission(String roleName, PermissionType permissionType);

    /**
     * 어떤 역할에 어떤 권한들을 추가
     */
    void addPermissions(String roleName, Collection<PermissionType> permissionTypes);

    /**
     * 어떤 역할에 어떤 권한을 제거
     */
    void deletePermission(String roleName, PermissionType permissionType);

    void deletePermissions(String roleName, Collection<PermissionType> permissionTypes);

    void changePermission(String roleName, Collection<PermissionType> newPermissions);

    /**
     * 여러 역할에 부여된 모든 권한들을 조회
     */
    Set<PermissionType> getPermissionsByRoles(Collection<String> roleNames);
}
