package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface UserRoleService {
    List<String> getAllRole(Long userId);
    boolean hasRole(Long userId, String roleName);
    void addRole(Long userId, String roleName);
    void addRoles(Long userId, Collection<String> roleNames);
    void deleteRole(Long userId, String roleName);
    void deleteRoles(Long userId, Collection<String> roleNames);
    void changeRole(Long userId, Collection<String> newRoleNames);
}
