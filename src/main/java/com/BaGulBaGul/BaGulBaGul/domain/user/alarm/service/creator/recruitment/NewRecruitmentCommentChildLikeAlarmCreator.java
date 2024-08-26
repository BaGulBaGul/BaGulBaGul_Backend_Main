package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.recruitment;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.AlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.post.NewCommentChildLikeAlarmCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class NewRecruitmentCommentChildLikeAlarmCreator extends AlarmCreator {
    @Builder
    public NewRecruitmentCommentChildLikeAlarmCreator(
            NewCommentChildLikeAlarmCreator newCommentChildLikeAlarmCreator
    ) {
        this.type = AlarmType.NEW_RECRUITMENT_COMMENT_CHILD_LIKE;
        this.targetUserId = newCommentChildLikeAlarmCreator.getTargetUserId();
        this.title = newCommentChildLikeAlarmCreator.getTitle();
        this.message = newCommentChildLikeAlarmCreator.getMessage();
        this.time = newCommentChildLikeAlarmCreator.getTime();

        Subject subjectObject = Subject.builder()
                .targetSubject(newCommentChildLikeAlarmCreator.getSubjectObject())
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
        public Subject(NewCommentChildLikeAlarmCreator.Subject targetSubject) {
            this.commentId = targetSubject.getCommentId();
        }
    }
}
