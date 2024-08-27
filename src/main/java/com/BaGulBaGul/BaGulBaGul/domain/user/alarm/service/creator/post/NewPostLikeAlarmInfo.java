package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.post;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.AlarmInfo;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.event.NewEventLikeAlarmCreator.Subject;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NewPostLikeAlarmInfo extends AlarmInfo {

    private static final String titleFormat = "%s 글에 좋아요 %d개가 눌렸어요";

    @Builder
    public NewPostLikeAlarmInfo(
            Long targetUserId,
            LocalDateTime time,
            String postTitle,
            int likeCount
    ) {
        this.time = time;
        this.targetUserId = targetUserId;
        this.title = makeAlarmTitle(postTitle, likeCount);
        this.message = null;
    }

    private String makeAlarmTitle(String postTitle, int likeCount) {
        return String.format(titleFormat, postTitle, likeCount);
    }
}
