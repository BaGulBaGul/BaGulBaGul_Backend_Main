package com.BaGulBaGul.BaGulBaGul.domain.user.repository;

import com.BaGulBaGul.BaGulBaGul.domain.user.SocialLoginUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialLoginUserRepository extends JpaRepository<SocialLoginUser, String> {
    @EntityGraph(attributePaths = {"user"})
    Optional<SocialLoginUser> findWithUserById(String id);
}
