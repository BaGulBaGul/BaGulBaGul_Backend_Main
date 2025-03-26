package com.BaGulBaGul.BaGulBaGul.domain.calendar.event.controller;

import com.BaGulBaGul.BaGulBaGul.domain.calendar.event.dto.EventCalendarExistsResponse;
import com.BaGulBaGul.BaGulBaGul.domain.calendar.event.dto.EventCalendarSearchRequest;
import com.BaGulBaGul.BaGulBaGul.domain.calendar.event.dto.EventCalendarSearchResponse;
import com.BaGulBaGul.BaGulBaGul.domain.calendar.event.service.EventCalendarService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/calendar")
@RequiredArgsConstructor
@Api(tags = "캘린더")
public class EventCalendarControllerImpl implements EventCalendarController {

    private final EventCalendarService calendarService;

    @Override
    @GetMapping("/event")
    @Operation(summary = "캘린더에 등록된 이벤트 중 검색 시작 시간과 검색 종료 시간 사이에 있는 이벤트 정보를 검색",
            description = "로그인 필요"
    )
    public ApiResponse<List<EventCalendarSearchResponse>> searchEventByCondition(
            @AuthenticationPrincipal Long userId,
            EventCalendarSearchRequest eventCalendarSearchRequest
    ) {
        return ApiResponse.of(
                calendarService.findEventCalendarByCondition(userId, eventCalendarSearchRequest)
        );
    }

    @Override
    @GetMapping("/event/{eventId}/exists")
    @Operation(summary = "캘린더에 특정 이벤트가 존재하는지 확인",
            description = "로그인 필요"
    )
    public ApiResponse<EventCalendarExistsResponse> existsEventCalendar(
            @AuthenticationPrincipal Long userId,
            @PathVariable("eventId") Long eventId
    ) {
        return ApiResponse.of(
                EventCalendarExistsResponse.builder()
                        .exists(calendarService.existsEventCalendar(userId, eventId))
                        .build()
        );
    }

    @Override
    @PostMapping("/event/{eventId}")
    @Operation(summary = "캘린더에 이벤트를 등록",
            description = "이벤트 id를 받아서 등록. 로그인 필요"
    )
    public ApiResponse<Object> registerEventCalendar(
            @AuthenticationPrincipal Long userId,
            @PathVariable("eventId") Long eventId
    ) {
        calendarService.registerEventCalendar(userId, eventId);
        return ApiResponse.of(null);
    }

    @Override
    @DeleteMapping("/event/{eventId}")
    @Operation(summary = "캘린더에서 이벤트를 삭제",
            description = "이벤트 id를 받아서 삭제. 로그인 필요"
    )
    public ApiResponse<Object> deleteEventCalendar(
            @AuthenticationPrincipal Long userId,
            @PathVariable("eventId") Long eventId
    ) {
        calendarService.deleteEventCalendar(userId, eventId);
        return ApiResponse.of(null);
    }
}
