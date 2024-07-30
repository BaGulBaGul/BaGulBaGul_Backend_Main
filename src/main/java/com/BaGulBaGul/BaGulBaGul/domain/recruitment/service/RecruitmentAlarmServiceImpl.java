package com.BaGulBaGul.BaGulBaGul.domain.recruitment.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent.NewRecruitmentLikeApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.RecruitmentRepository;
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
        //타입
        AlarmType type = AlarmType.NEW_RECRUITMENT_LIKE;
        //알람 대상 = 모집글 작성자
        User targetUser = post.getUser();
        //제목
        String title = getNewRecruitmentLikeAlarmTitle(post.getTitle(), post.getLikeCount());
        //메세지
        String message = null;
        //참조 대상 = 모집글 id
        String subjectId = recruitment.getId().toString();
        alarmService.registerAlarm(targetUser, type, title, message, subjectId, newRecruitmentLikeApplicationEvent.getTime());
    }

    private String getNewRecruitmentLikeAlarmTitle(String postTitle, int likeCount) {
        return new StringBuilder().append(postTitle).append(" 글에 좋아요 ").append(likeCount).append("개가 눌렸어요").toString();
    }
}
