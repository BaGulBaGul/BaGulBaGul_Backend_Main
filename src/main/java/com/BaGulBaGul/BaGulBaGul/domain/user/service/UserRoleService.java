package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import java.util.List;

public interface UserRoleService {
    List<String> getAllRole(Long userId);
    boolean hasRole(Long userId, String roleName);
    void addRole(Long userId, String roleName);
    void addRoles(Long userId, List<String> roleNames);
    void deleteRole(Long userId, String roleName);
    void deleteRoles(Long userId, List<String> roleNames);
    void changeRole(Long userId, List<String> newRoleNames);
}
