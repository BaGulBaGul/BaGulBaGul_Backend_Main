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