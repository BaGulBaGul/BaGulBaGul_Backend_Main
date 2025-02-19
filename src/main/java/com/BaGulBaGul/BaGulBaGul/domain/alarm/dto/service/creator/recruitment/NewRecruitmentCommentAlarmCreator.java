package com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.service.creator.recruitment;

import com.BaGulBaGul.BaGulBaGul.domain.alarm.constant.AlarmType;
import com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.service.creator.AlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.service.creator.post.NewCommentAlarmInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class NewRecruitmentCommentAlarmCreator extends AlarmCreator {

    @Builder
    public NewRecruitmentCommentAlarmCreator(
            Long recruitmentId,
            NewCommentAlarmInfo newCommentAlarmInfo
    ) {
        this.type = AlarmType.NEW_RECRUITMENT_COMMENT;
        this.targetUserId = newCommentAlarmInfo.getTargetUserId();
        this.title = newCommentAlarmInfo.getTitle();
        this.message = newCommentAlarmInfo.getMessage();
        this.time = newCommentAlarmInfo.getTime();

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
