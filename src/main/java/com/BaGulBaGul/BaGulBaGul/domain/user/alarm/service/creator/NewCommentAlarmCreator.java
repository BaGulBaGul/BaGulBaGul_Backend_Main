package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import java.time.LocalDateTime;
import lombok.Builder;

public class NewCommentAlarmCreator extends AlarmCreator {

    private static final String titleFormat = "{0} 글에 댓글이 달렸어요";

    @Builder
    public NewCommentAlarmCreator(
            Long targetUserId,
            LocalDateTime time,
            Long postId,
            String postTitle,
            String commentContent
    ) {
        this.type = AlarmType.NEW_COMMENT;
        this.time = time;
        this.targetUserId = targetUserId;
        this.title = makeAlarmTitle(postTitle);
        this.message = commentContent;
        this.subject = postId.toString();
    }

    private String makeAlarmTitle(String postTitle) {
        return String.format(titleFormat, new Object[]{postTitle});
    }
}
