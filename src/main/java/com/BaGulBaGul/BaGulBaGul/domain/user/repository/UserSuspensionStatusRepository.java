package com.BaGulBaGul.BaGulBaGul.domain.user.repository;

import com.BaGulBaGul.BaGulBaGul.domain.user.UserSuspensionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSuspensionStatusRepository extends JpaRepository<UserSuspensionStatus, Long> {
}
