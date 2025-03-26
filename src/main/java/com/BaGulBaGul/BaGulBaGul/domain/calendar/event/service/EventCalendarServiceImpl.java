package com.BaGulBaGul.BaGulBaGul.domain.calendar.event.service;

import com.BaGulBaGul.BaGulBaGul.domain.calendar.event.dto.EventCalendarSearchRequest;
import com.BaGulBaGul.BaGulBaGul.domain.calendar.event.repository.EventCalendarRepository;
import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.exception.EventNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventRepository;
import com.BaGulBaGul.BaGulBaGul.domain.calendar.EventCalendar;
import com.BaGulBaGul.BaGulBaGul.domain.calendar.EventCalendar.EventCalendarId;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.calendar.event.dto.EventCalendarSearchResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.UserNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventCalendarServiceImpl implements EventCalendarService {

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
        if(event.getDeleted()) {
            throw new EventNotFoundException();
        }
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
