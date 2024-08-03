package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import java.time.LocalDateTime;
import lombok.Builder;

public class NewCommentLikeAlarmCreator extends AlarmCreator {

    private static final String titleFormat = "작성하신 댓글에 좋아요 {0}개가 눌렸어요";

    @Builder
    public NewCommentLikeAlarmCreator(
            Long targetUserId,
            LocalDateTime time,
            Long commentId,
            String commentContent,
            int commentLikeCount
    ) {
        this.type = AlarmType.NEW_COMMENT_LIKE;
        this.time = time;
        this.targetUserId = targetUserId;
        this.title = makeAlarmTitle(commentLikeCount);
        this.message = commentContent;
        this.subject = commentId.toString();
    }

    private String makeAlarmTitle(int likeCount) {
        return String.format(titleFormat, new Object[]{likeCount});
    }
}
