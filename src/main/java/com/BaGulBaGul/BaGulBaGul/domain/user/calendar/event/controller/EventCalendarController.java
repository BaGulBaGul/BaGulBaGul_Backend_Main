package com.BaGulBaGul.BaGulBaGul.domain.user.calendar.event.controller;

import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.event.dto.EventCalendarExistsResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.event.dto.EventCalendarSearchRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.event.dto.EventCalendarSearchResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import java.util.List;

public interface EventCalendarController {
    ApiResponse<List<EventCalendarSearchResponse>> searchEventByCondition(Long userId, EventCalendarSearchRequest eventCalendarSearchRequest);
    ApiResponse<EventCalendarExistsResponse> existsEventCalendar(Long userId, Long eventId);
    ApiResponse<Object> registerEventCalendar(Long userId, Long eventId);
    ApiResponse<Object> deleteEventCalendar(Long userId, Long eventId);
}
