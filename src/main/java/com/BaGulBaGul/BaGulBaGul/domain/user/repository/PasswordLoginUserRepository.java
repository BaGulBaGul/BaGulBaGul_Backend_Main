package com.BaGulBaGul.BaGulBaGul.domain.user.repository;

import com.BaGulBaGul.BaGulBaGul.domain.user.PasswordLoginUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordLoginUserRepository extends JpaRepository<PasswordLoginUser, String> {
}
