package com.BaGulBaGul.BaGulBaGul.domain.event.applicationevent.listener;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.applicationevent.NewEventCommentApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.event.applicationevent.NewEventCommentChildApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.event.applicationevent.NewEventCommentChildLikeApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.event.applicationevent.NewEventCommentLikeApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.event.applicationevent.NewEventLikeApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostAlarmService;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.AlarmService;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.AlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.event.NewEventCommentAlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.event.NewEventCommentChildAlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.event.NewEventCommentChildLikeAlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.event.NewEventCommentLikeAlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.event.NewEventLikeAlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.post.NewCommentAlarmInfo;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.post.NewCommentChildAlarmInfo;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.post.NewCommentChildLikeAlarmInfo;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.post.NewCommentLikeAlarmInfo;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.post.NewPostLikeAlarmInfo;
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
    private final PostAlarmService postAlarmService;

    //이벤트에 좋아요 추가 시 이벤트 작성자에게 알람
    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async
    public void alarmToEventWriter(
            NewEventLikeApplicationEvent newEventLikeApplicationEvent
    ) {
        //좋아요를 받은 이벤트를 조회
        Event likedEvent = eventRepository
                .findById(newEventLikeApplicationEvent.getEventId())
                .orElse(null);
        //이벤트가 없을 경우
        if(likedEvent == null) {
            return;
        }
        //이벤트의 게시글
        Post post = likedEvent.getPost();

        NewPostLikeAlarmInfo alarmInfo = postAlarmService.getNewPostLikeAlarmInfo(
                newEventLikeApplicationEvent.getTime(),
                post.getId()
        );
        if(alarmInfo == null) {
            return;
        }
        AlarmCreator alarmCreator = NewEventLikeAlarmCreator.builder()
                .eventId(newEventLikeApplicationEvent.getEventId())
                .newPostLikeAlarmInfo(alarmInfo)
                .build();
        alarmService.registerAlarm(alarmCreator);
    }

    //이벤트에 댓글 추가 시 이벤트 작성자에게 알람
    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async
    public void alarmToEventWriter(
            NewEventCommentApplicationEvent newEventCommentApplicationEvent
    ) {
        NewCommentAlarmInfo alarmInfo = postAlarmService.getNewCommentAlarmInfo(
                newEventCommentApplicationEvent.getTime(),
                newEventCommentApplicationEvent.getNewCommentId()
        );
        if(alarmInfo == null) {
            return;
        }
        AlarmCreator alarmCreator = NewEventCommentAlarmCreator.builder()
                .eventId(newEventCommentApplicationEvent.getEventId())
                .newCommentAlarmInfo(alarmInfo)
                .build();
        alarmService.registerAlarm(alarmCreator);
    }

    //이벤트 댓글에 좋아요 추가 시 댓글 작성자에게 알람
    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async
    public void alarmToEventCommentWriter(
            NewEventCommentLikeApplicationEvent newEventCommentLikeApplicationEvent
    ) {
        NewCommentLikeAlarmInfo alarmInfo = postAlarmService.getNewCommentLikeAlarmInfo(
                newEventCommentLikeApplicationEvent.getTime(),
                newEventCommentLikeApplicationEvent.getLikedCommentId()
        );
        if(alarmInfo == null) {
            return;
        }
        AlarmCreator alarmCreator = NewEventCommentLikeAlarmCreator.builder()
                .newCommentLikeAlarmInfo(alarmInfo)
                .build();
        alarmService.registerAlarm(alarmCreator);
    }

    //이벤트 댓글에 대댓글 추가 시 댓글 작성자에게 알람
    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async
    public void alarmToEventCommentWriter(
            NewEventCommentChildApplicationEvent newEventCommentChildApplicationEvent
    ) {
        NewCommentChildAlarmInfo alarmInfo = postAlarmService.getNewCommentChildAlarmInfo(
                newEventCommentChildApplicationEvent.getTime(),
                newEventCommentChildApplicationEvent.getNewCommentChildId()
        );
        if(alarmInfo == null) {
            return;
        }
        AlarmCreator alarmCreator = NewEventCommentChildAlarmCreator.builder()
                .newCommentChildAlarmInfo(alarmInfo)
                .build();
        alarmService.registerAlarm(alarmCreator);
    }

    //이벤트 대댓글에 좋아요 추가 시 대댓글 작성자에게 알람
    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async
    public void alarmToEventCommentChildWriter(
            NewEventCommentChildLikeApplicationEvent newEventCommentChildLikeApplicationEvent
    ) {
        NewCommentChildLikeAlarmInfo alarmInfo = postAlarmService.getNewCommentChildLikeAlarmInfo(
                newEventCommentChildLikeApplicationEvent.getTime(),
                newEventCommentChildLikeApplicationEvent.getLikedCommentChildId()
        );
        if(alarmInfo == null) {
            return;
        }
        AlarmCreator alarmCreator = NewEventCommentChildLikeAlarmCreator.builder()
                .newCommentChildLikeAlarmInfo(alarmInfo)
                .build();
        alarmService.registerAlarm(alarmCreator);
    }

    //이벤트 대댓글에 답글 작성 시 대댓글 작성자에게 알람
    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async
    public void alarmToEventCommentChildWriter(
            NewEventCommentChildApplicationEvent newEventCommentChildApplicationEvent
    ) {
        NewCommentChildAlarmInfo alarmInfo = postAlarmService.getNewCommentChildAlarmInfoIfReply(
                newEventCommentChildApplicationEvent.getTime(),
                newEventCommentChildApplicationEvent.getNewCommentChildId(),
                newEventCommentChildApplicationEvent.getReplyTargetCommentChildId()
        );
        if(alarmInfo == null) {
            return;
        }
        AlarmCreator alarmCreator = NewEventCommentChildAlarmCreator.builder()
                .newCommentChildAlarmInfo(alarmInfo)
                .build();
        alarmService.registerAlarm(alarmCreator);
    }
}
