package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import java.time.LocalDateTime;

public class NewRecruitmentLikeAlarmCreator extends AlarmCreator {
    private static final String titleFormat = "{0} 글에 좋아요 {1}개가 눌렸어요";

    public NewRecruitmentLikeAlarmCreator(
            Long targetUserId,
            LocalDateTime time,
            Long recruitmentId,
            String postTitle,
            int likeCount
    ) {
        this.type = AlarmType.NEW_RECRUITMENT_LIKE;
        this.time = time;
        this.targetUserId = targetUserId;
        this.title = makeAlarmTitle(postTitle, likeCount);
        this.message = null;
        this.subject = recruitmentId.toString();
    }
    private String makeAlarmTitle(String postTitle, int likeCount) {
        return String.format(titleFormat, new Object[]{postTitle, likeCount});
    }
}
