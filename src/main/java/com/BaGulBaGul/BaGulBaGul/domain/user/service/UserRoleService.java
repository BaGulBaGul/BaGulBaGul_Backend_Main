package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface UserRoleService {
    List<String> getAllRole(Long userId);
    boolean hasRole(Long userId, String roleName);
    void addRole(AuthenticatedUserInfo requestUserAuthInfo, Long userId, String roleName);
    void addRole(Long userId, String roleName);
    void addRoles(AuthenticatedUserInfo requestUserAuthInfo, Long userId, Collection<String> roleNames);
    void addRoles(Long userId, Collection<String> roleNames);
    void deleteRole(AuthenticatedUserInfo requestUserAuthInfo, Long userId, String roleName);
    void deleteRole(Long userId, String roleName);
    void deleteRoles(AuthenticatedUserInfo requestUserAuthInfo, Long userId, Collection<String> roleNames);
    void deleteRoles(Long userId, Collection<String> roleNames);
    void changeRole(AuthenticatedUserInfo requestUserAuthInfo, Long userId, Collection<String> newRoleNames);
    void changeRole(Long userId, Collection<String> newRoleNames);
}
