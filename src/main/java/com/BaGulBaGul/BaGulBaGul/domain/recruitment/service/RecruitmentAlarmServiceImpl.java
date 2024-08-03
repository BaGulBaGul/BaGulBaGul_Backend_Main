package com.BaGulBaGul.BaGulBaGul.domain.recruitment.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent.NewRecruitmentLikeApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.RecruitmentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.AlarmService;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.AlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.NewEventLikeAlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.NewRecruitmentLikeAlarmCreator;
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

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async
    public void alarmToRecruitmentWriter(NewRecruitmentLikeApplicationEvent newRecruitmentLikeApplicationEvent) {
        Recruitment recruitment = recruitmentRepository.findById(newRecruitmentLikeApplicationEvent.getRecruitmentId()).orElse(null);
        if(recruitment == null) {
            return;
        }
        Post post = recruitment.getPost();
        AlarmCreator alarmCreator = NewRecruitmentLikeAlarmCreator.builder()
                .targetUserId(post.getUser().getId())
                .time(newRecruitmentLikeApplicationEvent.getTime())
                .recruitmentId(newRecruitmentLikeApplicationEvent.getRecruitmentId())
                .postTitle(post.getTitle())
                .likeCount(post.getLikeCount())
                .build();
        alarmService.registerAlarm(alarmCreator);
    }
}
