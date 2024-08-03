package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import java.time.LocalDateTime;
import lombok.Builder;

public class NewCommentChildLikeAlarmCreator extends AlarmCreator {
    private static final String titleFormat = "작성하신 댓글에 좋아요 %d개가 눌렸어요";

    @Builder
    public NewCommentChildLikeAlarmCreator(
            Long targetUserId,
            LocalDateTime time,
            Long commentId,
            String commentChildContent,
            int commentChildLikeCount
    ) {
        this.type = AlarmType.NEW_COMMENT_CHILD_LIKE;
        this.time = time;
        this.targetUserId = targetUserId;
        this.title = makeAlarmTitle(commentChildLikeCount);
        this.message = commentChildContent;
        this.subject = commentId.toString();
    }

    private String makeAlarmTitle(int likeCount) {
        return String.format(titleFormat, likeCount);
    }
}
