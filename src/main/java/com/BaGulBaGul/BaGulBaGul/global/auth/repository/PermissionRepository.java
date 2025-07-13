package com.BaGulBaGul.BaGulBaGul.global.auth.repository;

import com.BaGulBaGul.BaGulBaGul.global.auth.Permission;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.PermissionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, String> {
}
