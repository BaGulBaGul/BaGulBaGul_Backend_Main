package com.BaGulBaGul.BaGulBaGul.domain.user.calendar.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.dto.EventCalendarDeleteRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.dto.EventCalendarRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.dto.EventCalendarSearchRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.dto.EventCalendarSearchResponse;
import java.util.List;

public interface CalendarService {
    List<EventCalendarSearchResponse> findEventCalendarByCondition(Long userId, EventCalendarSearchRequest eventCalendarSearchRequest);
    void registerEventCalendar(Long userId, EventCalendarRegisterRequest eventCalendarRegisterRequest);
    void deleteEventCalendar(Long userId, EventCalendarDeleteRequest eventCalendarDeleteRequest);
}
