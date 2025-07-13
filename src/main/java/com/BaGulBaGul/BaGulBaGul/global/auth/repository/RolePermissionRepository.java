package com.BaGulBaGul.BaGulBaGul.global.auth.repository;

import com.BaGulBaGul.BaGulBaGul.global.auth.Role;
import com.BaGulBaGul.BaGulBaGul.global.auth.RolePermission;
import com.BaGulBaGul.BaGulBaGul.global.auth.RolePermission.RolePermissionId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {

    List<RolePermission> findAllByRole(Role role);
}
