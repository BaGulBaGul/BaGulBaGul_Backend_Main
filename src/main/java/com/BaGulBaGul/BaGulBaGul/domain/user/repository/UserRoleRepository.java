package com.BaGulBaGul.BaGulBaGul.domain.user.repository;

import com.BaGulBaGul.BaGulBaGul.domain.user.UserRole;
import com.BaGulBaGul.BaGulBaGul.domain.user.UserRole.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
}
