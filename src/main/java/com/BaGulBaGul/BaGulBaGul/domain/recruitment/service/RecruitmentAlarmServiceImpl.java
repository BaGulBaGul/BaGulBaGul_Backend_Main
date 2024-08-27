package com.BaGulBaGul.BaGulBaGul.domain.recruitment.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostComment;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentChild;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentChildRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostAlarmService;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent.NewRecruitmentCommentApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent.NewRecruitmentCommentChildApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent.NewRecruitmentCommentChildLikeApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent.NewRecruitmentCommentLikeApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent.NewRecruitmentLikeApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.RecruitmentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
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
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.recruitment.NewRecruitmentCommentAlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.recruitment.NewRecruitmentCommentChildAlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.recruitment.NewRecruitmentCommentChildLikeAlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.recruitment.NewRecruitmentCommentLikeAlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.recruitment.NewRecruitmentLikeAlarmCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class RecruitmentAlarmServiceImpl implements RecruitmentAlarmService {

    private final RecruitmentRepository recruitmentRepository;

    private final AlarmService alarmService;
    private final PostAlarmService postAlarmService;

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async
    public void alarmToRecruitmentWriter(NewRecruitmentLikeApplicationEvent newRecruitmentLikeApplicationEvent) {
        //좋아요를 받은 모집글을 조회
        Recruitment likedRecruitment = recruitmentRepository
                .findById(newRecruitmentLikeApplicationEvent.getRecruitmentId())
                .orElse(null);
        //모집글이 없을 경우
        if(likedRecruitment == null) {
            return;
        }
        //모집글의 게시글
        Post post = likedRecruitment.getPost();

        NewPostLikeAlarmInfo alarmInfo = postAlarmService.getNewPostLikeAlarmInfo(
                newRecruitmentLikeApplicationEvent.getTime(),
                post.getId()
        );
        if(alarmInfo == null) {
            return;
        }
        AlarmCreator alarmCreator = NewRecruitmentLikeAlarmCreator.builder()
                .recruitmentId(newRecruitmentLikeApplicationEvent.getRecruitmentId())
                .newPostLikeAlarmInfo(alarmInfo)
                .build();
        alarmService.registerAlarm(alarmCreator);
    }

    //모집글에 댓글 추가 시 모집글 작성자에게 알람
    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async
    public void alarmToRecruitmentWriter(
            NewRecruitmentCommentApplicationEvent newRecruitmentCommentApplicationEvent
    ) {
        NewCommentAlarmInfo alarmInfo = postAlarmService.getNewCommentAlarmInfo(
                newRecruitmentCommentApplicationEvent.getTime(),
                newRecruitmentCommentApplicationEvent.getNewCommentId()
        );
        if(alarmInfo == null) {
            return;
        }
        AlarmCreator alarmCreator = NewRecruitmentCommentAlarmCreator.builder()
                .recruitmentId(newRecruitmentCommentApplicationEvent.getRecruitmentId())
                .newCommentAlarmInfo(alarmInfo)
                .build();
        alarmService.registerAlarm(alarmCreator);
    }

    //모집글 댓글에 좋아요 추가 시 댓글 작성자에게 알람
    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async
    public void alarmToRecruitmentCommentWriter(
            NewRecruitmentCommentLikeApplicationEvent newRecruitmentCommentLikeApplicationEvent
    ) {
        NewCommentLikeAlarmInfo alarmInfo = postAlarmService.getNewCommentLikeAlarmInfo(
                newRecruitmentCommentLikeApplicationEvent.getTime(),
                newRecruitmentCommentLikeApplicationEvent.getLikedCommentId()
        );
        if(alarmInfo == null) {
            return;
        }
        AlarmCreator alarmCreator = NewRecruitmentCommentLikeAlarmCreator.builder()
                .newCommentLikeAlarmInfo(alarmInfo)
                .build();
        alarmService.registerAlarm(alarmCreator);
    }

    //모집글 댓글에 대댓글 추가 시 댓글 작성자에게 알람
    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async
    public void alarmToRecruitmentCommentWriter(
            NewRecruitmentCommentChildApplicationEvent newRecruitmentCommentChildApplicationEvent
    ) {
        NewCommentChildAlarmInfo alarmInfo = postAlarmService.getNewCommentChildAlarmInfo(
                newRecruitmentCommentChildApplicationEvent.getTime(),
                newRecruitmentCommentChildApplicationEvent.getNewCommentChildId()
        );
        if(alarmInfo == null) {
            return;
        }
        AlarmCreator alarmCreator = NewRecruitmentCommentChildAlarmCreator.builder()
                .newCommentChildAlarmInfo(alarmInfo)
                .build();
        alarmService.registerAlarm(alarmCreator);
    }

    //모집글 대댓글에 좋아요 추가 시 대댓글 작성자에게 알람
    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async
    public void alarmToRecruitmentCommentChildWriter(
            NewRecruitmentCommentChildLikeApplicationEvent newRecruitmentCommentChildLikeApplicationEvent
    ) {
        NewCommentChildLikeAlarmInfo alarmInfo = postAlarmService.getNewCommentChildLikeAlarmInfo(
                newRecruitmentCommentChildLikeApplicationEvent.getTime(),
                newRecruitmentCommentChildLikeApplicationEvent.getLikedCommentChildId()
        );
        if(alarmInfo == null) {
            return;
        }
        AlarmCreator alarmCreator = NewRecruitmentCommentChildLikeAlarmCreator.builder()
                .newCommentChildLikeAlarmInfo(alarmInfo)
                .build();
        alarmService.registerAlarm(alarmCreator);
    }

    //모집글 대댓글에 답글 작성 시 대댓글 작성자에게 알람
    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async
    public void alarmToRecruitmentCommentChildWriter(
            NewRecruitmentCommentChildApplicationEvent newRecruitmentCommentChildApplicationEvent
    ) {
        NewCommentChildAlarmInfo alarmInfo = postAlarmService.getNewCommentChildAlarmInfoIfReply(
                newRecruitmentCommentChildApplicationEvent.getTime(),
                newRecruitmentCommentChildApplicationEvent.getNewCommentChildId(),
                newRecruitmentCommentChildApplicationEvent.getReplyTargetCommentChildId()
        );
        if(alarmInfo == null) {
            return;
        }
        AlarmCreator alarmCreator = NewRecruitmentCommentChildAlarmCreator.builder()
                .newCommentChildAlarmInfo(alarmInfo)
                .build();
        alarmService.registerAlarm(alarmCreator);
    }
}
