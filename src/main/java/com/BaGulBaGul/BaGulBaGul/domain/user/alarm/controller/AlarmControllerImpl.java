package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.controller;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.dto.AlarmPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.dto.GetAlarmStatusResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.AlarmService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
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
@Api(tags = "알람")
public class AlarmControllerImpl implements AlarmController {

    private final AlarmService alarmService;

    @Override
    @GetMapping("/")
    @Operation(summary = "알람을 최신순으로 페이지조회",
            description = "로그인 필수."
    )
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
    @Operation(summary = "지정한 알람을 체크상태로 만든다",
            description = "로그인 필수."
    )
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
    @Operation(summary = "지정한 알람을 삭제한다",
            description = "로그인 필수."
    )
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
    @Operation(summary = "유저의 모든 알람을 삭제한다",
            description = "로그인 필수."
    )
    public ApiResponse<Object> deleteAllAlarm(@AuthenticationPrincipal Long userId) {
        alarmService.deleteAllAlarm(userId);
        return ApiResponse.of(
                null
        );
    }

    @Override
    @GetMapping("/status")
    @Operation(summary = "유저의 알람 상태를 조회",
            description = "로그인 필수. 총 알람 개수, 체크하지 않은 알람 등의 정보를 조회"
    )
    public ApiResponse<GetAlarmStatusResponse> getAlarmStatus(
            @AuthenticationPrincipal Long userId
    ) {
        return ApiResponse.of(
                alarmService.getAlarmStatus(userId)
        );
    }
}
