package com.BaGulBaGul.BaGulBaGul.domain.alarm.service;

import com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.service.creator.AlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.api.AlarmPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.api.GetAlarmStatusResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlarmService {
    void registerAlarm(AlarmCreator alarmCreator);
    Page<AlarmPageResponse> getAlarmPageByTime(Long userId, Pageable pageable);
    void checkAlarm(Long userId, Long alarmId);
    void deleteAlarm(Long userId, Long alarmId);
    void deleteAllAlarm(Long userId);
    GetAlarmStatusResponse getAlarmStatus(Long userId);
}
