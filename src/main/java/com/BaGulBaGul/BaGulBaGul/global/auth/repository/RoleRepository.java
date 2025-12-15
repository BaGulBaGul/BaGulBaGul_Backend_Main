package com.BaGulBaGul.BaGulBaGul.global.auth.repository;

import com.BaGulBaGul.BaGulBaGul.global.auth.Role;
import com.BaGulBaGul.BaGulBaGul.global.auth.repository.querydsl.FindRoleByCondition;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role, String>, FindRoleByCondition {
    @Query("SELECT r "
            + "FROM Role r "
                + "LEFT JOIN FETCH r.rolePermissions rp "
                + "LEFT JOIN FETCH rp.permission p "
            + "WHERE r.name in :roleNames")
    List<Role> findByIdsWithPermissions(@Param("roleNames") List<String> roleNames);
}
