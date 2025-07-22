package com.BaGulBaGul.BaGulBaGul.domain.calendar.event.controller;

import com.BaGulBaGul.BaGulBaGul.domain.calendar.event.dto.EventCalendarExistsResponse;
import com.BaGulBaGul.BaGulBaGul.domain.calendar.event.dto.EventCalendarSearchRequest;
import com.BaGulBaGul.BaGulBaGul.domain.calendar.event.dto.EventCalendarSearchResponse;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import java.util.List;

public interface EventCalendarController {
    ApiResponse<List<EventCalendarSearchResponse>> searchEventByCondition(
            AuthenticatedUserInfo authenticatedUserInfo,
            EventCalendarSearchRequest eventCalendarSearchRequest
    );
    ApiResponse<EventCalendarExistsResponse> existsEventCalendar(
            AuthenticatedUserInfo authenticatedUserInfo,
            Long eventId
    );
    ApiResponse<Object> registerEventCalendar(
            AuthenticatedUserInfo authenticatedUserInfo,
            Long eventId
    );
    ApiResponse<Object> deleteEventCalendar(
            AuthenticatedUserInfo authenticatedUserInfo,
            Long eventId
    );
}
