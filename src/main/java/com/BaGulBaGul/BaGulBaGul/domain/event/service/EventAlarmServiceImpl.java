package com.BaGulBaGul.BaGulBaGul.domain.event.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.applicationevent.NewEventLikeApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.AlarmService;
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
    public void alarmToEventWriter(NewEventLikeApplicationEvent newEventLikeApplicationEvent) {
        Event event = eventRepository.findById(newEventLikeApplicationEvent.getEventId()).orElse(null);
        if(event == null) {
            return;
        }
        Post post = event.getPost();
        //타입
        AlarmType type = AlarmType.NEW_EVENT_LIKE;
        //알람 대상 = 이벤트 작성자
        User targetUser = post.getUser();
        //제목
        String title = getNewEventLikeAlarmTitle(post.getTitle(), post.getLikeCount());
        //메세지
        String message = null;
        //참조 대상 = 이벤트 id
        String subjectId = event.getId().toString();
        alarmService.registerAlarm(targetUser, type, title, message, subjectId, newEventLikeApplicationEvent.getTime());
    }

    private String getNewEventLikeAlarmTitle(String postTitle, int likeCount) {
        return new StringBuilder().append(postTitle).append(" 글에 좋아요 ").append(likeCount).append("개가 눌렸어요").toString();
    }
}
