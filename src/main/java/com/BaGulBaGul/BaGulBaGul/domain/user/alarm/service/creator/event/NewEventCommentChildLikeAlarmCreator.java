package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.event;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.AlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.post.NewCommentChildLikeAlarmInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class NewEventCommentChildLikeAlarmCreator extends AlarmCreator {

    @Builder
    public NewEventCommentChildLikeAlarmCreator(
            NewCommentChildLikeAlarmInfo newCommentChildLikeAlarmInfo
    ) {
        this.type = AlarmType.NEW_EVENT_COMMENT_CHILD_LIKE;
        this.targetUserId = newCommentChildLikeAlarmInfo.getTargetUserId();
        this.title = newCommentChildLikeAlarmInfo.getTitle();
        this.message = newCommentChildLikeAlarmInfo.getMessage();
        this.time = newCommentChildLikeAlarmInfo.getTime();

        Subject subjectObject = Subject.builder()
                .targetSubject(newCommentChildLikeAlarmInfo.getSubjectObject())
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
        public Subject(NewCommentChildLikeAlarmInfo.Subject targetSubject) {
            this.commentId = targetSubject.getCommentId();
        }
    }
}
