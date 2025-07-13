package com.BaGulBaGul.BaGulBaGul.global.auth.repository;

import com.BaGulBaGul.BaGulBaGul.global.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
