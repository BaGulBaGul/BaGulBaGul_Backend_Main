package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import java.time.LocalDateTime;
import lombok.Builder;

public class NewCommentChildAlarmCreator extends AlarmCreator {

    @Builder
    public NewCommentChildAlarmCreator(
            Long targetUserId,
            LocalDateTime time,
            Long commentId,
            String commentChildContent
    ) {
        this.type = AlarmType.NEW_COMMENT_CHILD;
        this.time = time;
        this.targetUserId = targetUserId;
        this.title = "작성하신 댓글에 답글이 달렸어요";
        this.message = commentChildContent;
        this.subject = commentId.toString();
    }
}