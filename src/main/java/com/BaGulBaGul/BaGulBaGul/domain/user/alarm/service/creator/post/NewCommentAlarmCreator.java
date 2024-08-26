package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.post;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.AlarmCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class NewCommentAlarmCreator extends AlarmCreator {

    private static final String titleFormat = "%s 글에 댓글이 달렸어요";

    private Subject subjectObject;

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
        this.subjectObject = Subject.builder()
                .postId(postId)
                .build();
        try {
            this.subject = makeSubjectJSON(subjectObject);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("AlarmCreator subject json 변환 실패");
        }
    }

    private String makeAlarmTitle(String postTitle) {
        return String.format(titleFormat, postTitle);
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class Subject {
        Long postId;
    }
}
