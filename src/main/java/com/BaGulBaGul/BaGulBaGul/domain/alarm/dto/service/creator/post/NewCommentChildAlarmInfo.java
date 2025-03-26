package com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.service.creator.post;

import com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.service.creator.AlarmInfo;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NewCommentChildAlarmInfo extends AlarmInfo {

    private Long commentId;

    @Builder
    public NewCommentChildAlarmInfo(
            Long targetUserId,
            LocalDateTime time,
            Long commentId,
            String commentChildContent
    ) {
        this.time = time;
        this.targetUserId = targetUserId;
        this.title = "작성하신 댓글에 답글이 달렸어요";
        this.message = commentChildContent;

        this.commentId = commentId;
    }
}