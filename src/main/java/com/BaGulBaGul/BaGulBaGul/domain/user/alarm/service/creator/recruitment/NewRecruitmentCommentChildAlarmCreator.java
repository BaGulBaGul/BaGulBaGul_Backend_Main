package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.recruitment;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.AlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.post.NewCommentChildAlarmCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class NewRecruitmentCommentChildAlarmCreator extends AlarmCreator {

    @Builder
    public NewRecruitmentCommentChildAlarmCreator(
            NewCommentChildAlarmCreator newCommentChildAlarmCreator
    ) {
        this.type = AlarmType.NEW_RECRUITMENT_COMMENT_CHILD;
        this.targetUserId = newCommentChildAlarmCreator.getTargetUserId();
        this.title = newCommentChildAlarmCreator.getTitle();
        this.message = newCommentChildAlarmCreator.getMessage();
        this.time = newCommentChildAlarmCreator.getTime();

        Subject subjectObject = Subject.builder()
                .targetSubject(newCommentChildAlarmCreator.getSubjectObject())
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
        public Subject(NewCommentChildAlarmCreator.Subject targetSubject) {
            this.commentId = targetSubject.getCommentId();
        }
    }
}