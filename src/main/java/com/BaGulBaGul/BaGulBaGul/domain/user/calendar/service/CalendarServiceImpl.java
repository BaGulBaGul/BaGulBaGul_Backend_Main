package com.BaGulBaGul.BaGulBaGul.domain.user.calendar.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.exception.EventNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.EventCalendar;
import com.BaGulBaGul.BaGulBaGul.domain.user.EventCalendar.EventCalendarId;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.dto.EventCalendarSearchRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.dto.EventCalendarSearchResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.repository.EventCalendarRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.exception.UserNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final EventCalendarRepository eventCalendarRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public List<EventCalendarSearchResponse> findEventCalendarByCondition(
            Long userId,
            EventCalendarSearchRequest eventCalendarSearchRequest
    ) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        List<EventCalendar> eventCalendarList = eventCalendarRepository.findByCondition(
                user,
                eventCalendarSearchRequest.getSearchStartTime(),
                eventCalendarSearchRequest.getSearchEndTime()
        );
        return eventCalendarList.stream().map(EventCalendarSearchResponse::of).collect(Collectors.toList());
    }

    @Override
    public boolean existsEventCalendar(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        return eventCalendarRepository.existsById(
                new EventCalendarId(user.getId(), eventId)
        );
    }

    @Override
    @Transactional
    public void registerEventCalendar(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException());
        eventCalendarRepository.save(
                new EventCalendar(user, event)
        );
    }

    @Override
    public void deleteEventCalendar(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException());
        eventCalendarRepository.deleteById(new EventCalendarId(user.getId(), event.getId()));
    }
}
