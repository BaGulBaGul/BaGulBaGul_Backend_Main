package com.BaGulBaGul.BaGulBaGul.domain.alarm.repository;

import com.BaGulBaGul.BaGulBaGul.domain.alarm.UserAlarmStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserAlarmStatusRepository extends JpaRepository<UserAlarmStatus, Long> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update UserAlarmStatus status "
            + "set status.totalAlarmCount = status.totalAlarmCount + 1, "
            + "    status.uncheckedAlarmCount = status.uncheckedAlarmCount + 1 "
            + "where status.user.id = :userId")
    void increaseTotalAndUnchecked(@Param("userId") Long userId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update UserAlarmStatus status "
            + "set status.totalAlarmCount = status.totalAlarmCount - 1, "
            + "    status.uncheckedAlarmCount = status.uncheckedAlarmCount - 1 "
            + "where status.user.id = :userId")
    void decreaseTotalAndUnchecked(@Param("userId") Long userId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update UserAlarmStatus status "
            + "set status.totalAlarmCount = status.totalAlarmCount - 1 "
            + "where status.user.id = :userId")
    void decreaseTotal(@Param("userId") Long userId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update UserAlarmStatus status "
            + "set status.uncheckedAlarmCount = status.uncheckedAlarmCount - 1 "
            + "where status.user.id = :userId")
    void decreaseUnchecked(@Param("userId") Long userId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update UserAlarmStatus status "
            + "set status.totalAlarmCount = 0, "
            + "    status.uncheckedAlarmCount = 0 "
            + "where status.user.id = :userId")
    void resetStatus(@Param("userId") Long userId);
}
