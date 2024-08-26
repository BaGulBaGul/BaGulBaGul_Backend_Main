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
public class NewCommentChildAlarmCreator extends AlarmCreator {

    private Subject subjectObject;

    @Builder
    public NewCommentChildAlarmCreator(
            Long targetUserId,
            LocalDateTime time,
            Long commentId,
            String commentChildContent
    ) {
        this.type = AlarmType.NEW_COMMENT_CHILD;
        this.time = time;
        this.targetUserId = targetUserId;
        this.title = "작성하신 댓글에 답글이 달렸어요";
        this.message = commentChildContent;
        this.subjectObject = Subject.builder()
                .commentId(commentId)
                .build();
        try {
            this.subject = makeSubjectJSON(subjectObject);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("AlarmCreator subject json 변환 실패");
        }
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class Subject {
        Long commentId;
    }
}