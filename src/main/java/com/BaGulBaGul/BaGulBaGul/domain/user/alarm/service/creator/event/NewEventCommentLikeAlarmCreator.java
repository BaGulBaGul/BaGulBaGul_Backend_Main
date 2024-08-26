package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.event;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.AlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.post.NewCommentLikeAlarmCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class NewEventCommentLikeAlarmCreator extends AlarmCreator {

    @Builder
    public NewEventCommentLikeAlarmCreator(
            NewCommentLikeAlarmCreator newCommentLikeAlarmCreator
    ) {
        this.type = AlarmType.NEW_EVENT_COMMENT_LIKE;
        this.targetUserId = newCommentLikeAlarmCreator.getTargetUserId();
        this.title = newCommentLikeAlarmCreator.getTitle();
        this.message = newCommentLikeAlarmCreator.getMessage();
        this.time = newCommentLikeAlarmCreator.getTime();

        Subject subjectObject = Subject.builder()
                .targetSubject(newCommentLikeAlarmCreator.getSubjectObject())
                .build();
        try {
            this.subject = makeSubjectJSON(subjectObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("AlarmCreator subject json 변환 실패");
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Subject {
        Long commentId;
        @Builder
        public Subject(NewCommentLikeAlarmCreator.Subject targetSubject) {
            this.commentId = targetSubject.getCommentId();
        }
    }
}
