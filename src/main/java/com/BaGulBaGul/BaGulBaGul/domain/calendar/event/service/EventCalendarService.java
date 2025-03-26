package com.BaGulBaGul.BaGulBaGul.domain.calendar.event.service;

import com.BaGulBaGul.BaGulBaGul.domain.calendar.event.dto.EventCalendarSearchRequest;
import com.BaGulBaGul.BaGulBaGul.domain.calendar.event.dto.EventCalendarSearchResponse;
import java.util.List;

public interface EventCalendarService {
    List<EventCalendarSearchResponse> findEventCalendarByCondition(Long userId, EventCalendarSearchRequest eventCalendarSearchRequest);
    boolean existsEventCalendar(Long userId, Long eventId);
    void registerEventCalendar(Long userId, Long eventId);
    void deleteEventCalendar(Long userId, Long eventId);
}
