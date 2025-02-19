package com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.service.creator.post;

import com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.service.creator.AlarmInfo;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NewCommentLikeAlarmInfo extends AlarmInfo {

    private static final String titleFormat = "작성하신 댓글에 좋아요 %d개가 눌렸어요";

    private Long commentId;

    @Builder
    public NewCommentLikeAlarmInfo(
            Long targetUserId,
            LocalDateTime time,
            Long commentId,
            String commentContent,
            int commentLikeCount
    ) {
        this.time = time;
        this.targetUserId = targetUserId;
        this.title = makeAlarmTitle(commentLikeCount);
        this.message = commentContent;

        this.commentId = commentId;
    }

    private String makeAlarmTitle(int likeCount) {
        return String.format(titleFormat, likeCount);
    }
}
