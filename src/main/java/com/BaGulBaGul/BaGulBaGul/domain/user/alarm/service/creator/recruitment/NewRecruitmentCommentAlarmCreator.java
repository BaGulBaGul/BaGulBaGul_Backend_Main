package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.recruitment;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.AlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.post.NewCommentAlarmCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class NewRecruitmentCommentAlarmCreator extends AlarmCreator {

    @Builder
    public NewRecruitmentCommentAlarmCreator(
            Long recruitmentId,
            NewCommentAlarmCreator newCommentAlarmCreator
    ) {
        this.type = AlarmType.NEW_RECRUITMENT_COMMENT;
        this.targetUserId = newCommentAlarmCreator.getTargetUserId();
        this.title = newCommentAlarmCreator.getTitle();
        this.message = newCommentAlarmCreator.getMessage();
        this.time = newCommentAlarmCreator.getTime();

        Subject subjectObject = Subject.builder()
                .recruitmentId(recruitmentId)
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
        Long recruitmentId;
    }
}
