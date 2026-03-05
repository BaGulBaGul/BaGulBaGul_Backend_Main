package com.BaGulBaGul.BaGulBaGul.domain.user.repository;

import com.BaGulBaGul.BaGulBaGul.domain.user.Role;
import com.BaGulBaGul.BaGulBaGul.domain.user.RolePermission;
import com.BaGulBaGul.BaGulBaGul.domain.user.RolePermission.RolePermissionId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {

    List<RolePermission> findAllByRole(Role role);
}
