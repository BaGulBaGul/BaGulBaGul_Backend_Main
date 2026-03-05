package com.BaGulBaGul.BaGulBaGul.domain.user.repository;

import com.BaGulBaGul.BaGulBaGul.domain.user.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, String> {
}
