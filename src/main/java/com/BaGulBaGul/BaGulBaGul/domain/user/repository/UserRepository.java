package com.BaGulBaGul.BaGulBaGul.domain.user.repository;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.querydsl.FindUserByCondition;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>, FindUserByCondition {
    //대소문자 무시하고 중복 닉네임 검사
    boolean existsByNicknameIgnoreCase(String nickname);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userRoles WHERE u.id=:userId")
    Optional<User> findUserWithRoles(@Param("userId") Long userId);

    @Query(
            "SELECT u FROM User u "
                    + "LEFT JOIN FETCH u.userRoles "
                    + "LEFT JOIN FETCH u.userSuspensionStatus "
                    + "WHERE u.id in :userIds"
    )
    List<User> findUserWithRolesAndSuspensionStatusByIds(@Param("userIds") List<Long> userIds);
}
