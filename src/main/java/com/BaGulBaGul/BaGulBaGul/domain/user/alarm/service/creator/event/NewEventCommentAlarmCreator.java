package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.event;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.AlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.post.NewCommentAlarmCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class NewEventCommentAlarmCreator extends AlarmCreator {

    @Builder
    public NewEventCommentAlarmCreator(
            Long eventId,
            NewCommentAlarmCreator newCommentAlarmCreator
    ) {
        this.type = AlarmType.NEW_EVENT_COMMENT;
        this.targetUserId = newCommentAlarmCreator.getTargetUserId();
        this.title = newCommentAlarmCreator.getTitle();
        this.message = newCommentAlarmCreator.getMessage();
        this.time = newCommentAlarmCreator.getTime();

        Subject subjectObject = Subject.builder()
                .eventId(eventId)
                .build();
        try {
            this.subject = makeSubjectJSON(subjectObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("AlarmCreator subject json 변환 실패");
        }
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class Subject {
        Long eventId;
    }
}
