package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.dto.AlarmPageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlarmService {
    Page<AlarmPageResponse> getAlarmPageByTime(Long userId, Pageable pageable);
    void checkAlarm(Long userId, Long alarmId);
    void deleteAlarm(Long userId, Long alarmId);
    void deleteAllAlarm(Long userId);
}
