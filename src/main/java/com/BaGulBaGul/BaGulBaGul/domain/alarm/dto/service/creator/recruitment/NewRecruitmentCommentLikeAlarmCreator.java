package com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.service.creator.recruitment;

import com.BaGulBaGul.BaGulBaGul.domain.alarm.constant.AlarmType;
import com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.service.creator.AlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.service.creator.post.NewCommentLikeAlarmInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class NewRecruitmentCommentLikeAlarmCreator extends AlarmCreator {

    @Builder
    public NewRecruitmentCommentLikeAlarmCreator(
            NewCommentLikeAlarmInfo newCommentLikeAlarmInfo
    ) {
        this.type = AlarmType.NEW_RECRUITMENT_COMMENT_LIKE;
        this.targetUserId = newCommentLikeAlarmInfo.getTargetUserId();
        this.title = newCommentLikeAlarmInfo.getTitle();
        this.message = newCommentLikeAlarmInfo.getMessage();
        this.time = newCommentLikeAlarmInfo.getTime();

        Subject subjectObject = Subject.builder()
                .commentId(newCommentLikeAlarmInfo.getCommentId())
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
        Long commentId;
    }
}
