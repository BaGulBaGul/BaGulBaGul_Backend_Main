package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.controller;

import com.BaGulBaGul.BaGulBaGul.domain.user.Alarm;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.dto.AlarmPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.repository.AlarmRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.AlarmService;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/alarm")
@RequiredArgsConstructor
public class AlarmControllerImpl implements AlarmController {

    private final AlarmService alarmService;

    @Override
    @GetMapping("/")
    public ApiResponse<Page<AlarmPageResponse>> getAlarmPageByTime(
            @AuthenticationPrincipal Long userId,
            Pageable pageable
    ) {
        return ApiResponse.of(
                alarmService.getAlarmPageByTime(userId, pageable)
        );
    }

    @Override
    @PostMapping("/{alarmId}/check")
    public ApiResponse<Object> checkAlarm(
            @AuthenticationPrincipal Long userId,
            @PathVariable(name="alarmId") Long alarmId
    ) {
        alarmService.checkAlarm(userId, alarmId);
        return ApiResponse.of(
                null
        );
    }

    @Override
    @DeleteMapping("/{alarmId}")
    public ApiResponse<Object> deleteAlarm(
            @AuthenticationPrincipal Long userId,
            @PathVariable(name="alarmId") Long alarmId
    ) {
        alarmService.deleteAlarm(userId, alarmId);
        return ApiResponse.of(
                null
        );
    }

    @Override
    @DeleteMapping("/")
    public ApiResponse<Object> deleteAllAlarm(@AuthenticationPrincipal Long userId) {
        alarmService.deleteAllAlarm(userId);
        return ApiResponse.of(
                null
        );
    }
}
