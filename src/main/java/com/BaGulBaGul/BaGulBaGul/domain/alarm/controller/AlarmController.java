package com.BaGulBaGul.BaGulBaGul.domain.alarm.controller;

import com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.api.AlarmPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.api.GetAlarmStatusResponse;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlarmController {
    ApiResponse<Page<AlarmPageResponse>> getAlarmPageByTime(AuthenticatedUserInfo authenticatedUserInfo, Pageable pageable);
    ApiResponse<Object> checkAlarm(AuthenticatedUserInfo authenticatedUserInfo, Long alarmId);
    ApiResponse<Object> deleteAlarm(AuthenticatedUserInfo authenticatedUserInfo, Long alarmId);
    ApiResponse<Object> deleteAllAlarm(AuthenticatedUserInfo authenticatedUserInfo);
    ApiResponse<GetAlarmStatusResponse> getAlarmStatus(AuthenticatedUserInfo authenticatedUserInfo);
}
