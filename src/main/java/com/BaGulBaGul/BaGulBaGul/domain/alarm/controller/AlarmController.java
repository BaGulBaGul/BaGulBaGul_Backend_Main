package com.BaGulBaGul.BaGulBaGul.domain.alarm.controller;

import com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.api.AlarmPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.api.GetAlarmStatusResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlarmController {
    ApiResponse<Page<AlarmPageResponse>> getAlarmPageByTime(Long userId, Pageable pageable);
    ApiResponse<Object> checkAlarm(Long userId, Long alarmId);
    ApiResponse<Object> deleteAlarm(Long userId, Long alarmId);
    ApiResponse<Object> deleteAllAlarm(Long userId);
    ApiResponse<GetAlarmStatusResponse> getAlarmStatus(Long userId);
}
