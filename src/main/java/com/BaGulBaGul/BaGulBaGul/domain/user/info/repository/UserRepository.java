package com.BaGulBaGul.BaGulBaGul.domain.user.info.repository;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
