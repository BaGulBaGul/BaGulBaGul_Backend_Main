package com.BaGulBaGul.BaGulBaGul.domain.user.repository;

import com.BaGulBaGul.BaGulBaGul.domain.user.UserSuspensionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSuspensionLogRepository extends JpaRepository<UserSuspensionLog, Long> {
}
