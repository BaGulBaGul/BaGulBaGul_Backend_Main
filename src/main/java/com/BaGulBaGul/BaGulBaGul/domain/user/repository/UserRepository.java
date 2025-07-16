package com.BaGulBaGul.BaGulBaGul.domain.user.repository;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    //대소문자 무시하고 중복 닉네임 검사
    boolean existsByNicknameIgnoreCase(String nickname);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userRoles WHERE u.id=:userId")
    User findUserWithRoles(@Param("userId") Long userId);
}
