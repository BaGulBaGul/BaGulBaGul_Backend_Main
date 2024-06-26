package com.BaGulBaGul.BaGulBaGul.domain.user.calendar.controller;

import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.dto.EventCalendarDeleteRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.dto.EventCalendarExistsResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.dto.EventCalendarRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.dto.EventCalendarSearchRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.dto.EventCalendarSearchResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import java.util.List;

public interface CalendarController {
    ApiResponse<List<EventCalendarSearchResponse>> searchEventByCondition(Long userId, EventCalendarSearchRequest eventCalendarSearchRequest);
    ApiResponse<EventCalendarExistsResponse> existsEventCalendar(Long userId, Long eventId);
    ApiResponse<Object> registerEventCalendar(Long userId, Long eventId);
    ApiResponse<Object> deleteEventCalendar(Long userId, Long eventId);
}
