package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.repository;

import com.BaGulBaGul.BaGulBaGul.domain.user.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}
