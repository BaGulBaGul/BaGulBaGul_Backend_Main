package com.BaGulBaGul.BaGulBaGul.domain.user.info.repository;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    //대소문자 무시하고 중복 닉네임 검사
    boolean existsByNicknameIgnoreCase(String nickname);
}
