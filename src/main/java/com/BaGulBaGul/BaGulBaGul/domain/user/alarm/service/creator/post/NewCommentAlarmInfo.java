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
public class NewCommentAlarmInfo extends AlarmInfo {

    private static final String titleFormat = "%s 글에 댓글이 달렸어요";

    @Builder
    public NewCommentAlarmInfo(
            Long targetUserId,
            LocalDateTime time,
            String postTitle,
            String commentContent
    ) {
        this.time = time;
        this.targetUserId = targetUserId;
        this.title = makeAlarmTitle(postTitle);
        this.message = commentContent;
    }

    private String makeAlarmTitle(String postTitle) {
        return String.format(titleFormat, postTitle);
    }
}
