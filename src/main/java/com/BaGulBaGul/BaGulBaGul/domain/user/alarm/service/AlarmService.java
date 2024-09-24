package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.dto.AlarmPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.dto.GetAlarmStatusResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.AlarmCreator;
import java.time.LocalDateTime;
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
