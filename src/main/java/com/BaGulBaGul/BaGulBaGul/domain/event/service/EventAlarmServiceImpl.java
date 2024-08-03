package com.BaGulBaGul.BaGulBaGul.domain.event.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.applicationevent.NewEventLikeApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.AlarmService;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.AlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.NewEventLikeAlarmCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class EventAlarmServiceImpl implements EventAlarmService {
    private final EventRepository eventRepository;
    private final AlarmService alarmService;

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async
    public void alarmToEventWriter(
            NewEventLikeApplicationEvent newEventLikeApplicationEvent
    ) {
        Event event = eventRepository.findById(newEventLikeApplicationEvent.getEventId()).orElse(null);
        if(event == null) {
            return;
        }
        Post post = event.getPost();
        AlarmCreator alarmCreator = NewEventLikeAlarmCreator.builder()
                .targetUserId(post.getUser().getId())
                .time(newEventLikeApplicationEvent.getTime())
                .eventId(newEventLikeApplicationEvent.getEventId())
                .postTitle(post.getTitle())
                .likeCount(post.getLikeCount())
                .build();
        alarmService.registerAlarm(alarmCreator);
    }
}
