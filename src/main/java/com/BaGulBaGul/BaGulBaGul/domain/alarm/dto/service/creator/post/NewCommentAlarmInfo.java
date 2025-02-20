package com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.service.creator.post;

import com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.service.creator.AlarmInfo;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

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
