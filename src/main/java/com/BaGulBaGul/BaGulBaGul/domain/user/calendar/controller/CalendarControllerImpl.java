package com.BaGulBaGul.BaGulBaGul.domain.user.calendar.controller;

import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.dto.EventCalendarDeleteRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.dto.EventCalendarRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.dto.EventCalendarSearchRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.dto.EventCalendarSearchResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.service.CalendarService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/calendar")
@RequiredArgsConstructor
public class CalendarControllerImpl implements CalendarController {

    private final CalendarService calendarService;

    @Override
    @GetMapping("/event")
    public ApiResponse<List<EventCalendarSearchResponse>> searchEventByCondition(
            @AuthenticationPrincipal Long userId,
            EventCalendarSearchRequest eventCalendarSearchRequest
    ) {
        return ApiResponse.of(
                calendarService.findEventCalendarByCondition(userId, eventCalendarSearchRequest)
        );
    }

    @Override
    @PostMapping("/event")
    public ApiResponse<Object> registerEventCalendar(
            @AuthenticationPrincipal Long userId,
            EventCalendarRegisterRequest eventCalendarRegisterRequest
    ) {
        calendarService.registerEventCalendar(userId, eventCalendarRegisterRequest);
        return ApiResponse.of(null);
    }

    @Override
    @DeleteMapping("/event")
    public ApiResponse<Object> deleteEventCalendar(
            @AuthenticationPrincipal Long userId,
            EventCalendarDeleteRequest eventCalendarDeleteRequest
    ) {
        calendarService.deleteEventCalendar(userId, eventCalendarDeleteRequest);
        return ApiResponse.of(null);
    }
}
