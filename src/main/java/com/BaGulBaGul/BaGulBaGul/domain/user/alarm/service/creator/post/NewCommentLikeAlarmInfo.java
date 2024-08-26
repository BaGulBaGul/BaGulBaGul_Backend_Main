package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.post;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.AlarmInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
