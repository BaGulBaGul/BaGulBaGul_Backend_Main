package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.repository;

import com.BaGulBaGul.BaGulBaGul.domain.user.Alarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    @Query(
            value = "SELECT al FROM Alarm al WHERE al.user.id = :userId ORDER BY al.time DESC",
            countQuery = "SELECT status.totalAlarmCount FROM UserAlarmStatus status WHERE status.userId = :userId"
    )
    Page<Alarm> findAlarmPageOrderByTime(@Param("userId") Long userId, Pageable pageable);

    @Modifying
    @Query(
            value = "DELETE FROM Alarm al WHERE al.user.id = :userId"
    )
    void deleteAllByUserId(@Param("userId") Long userId);

    @Modifying
    @Query(
            value = "DELETE FROM Alarm al WHERE al.id = :alarmId and al.version = :version"
    )
    int deleteByAlarmIdAndVersion(@Param("alarmId") Long alarmId, @Param("version") int version);
}