package com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.service.creator.recruitment;

import com.BaGulBaGul.BaGulBaGul.domain.alarm.constant.AlarmType;
import com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.service.creator.AlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.service.creator.post.NewCommentChildLikeAlarmInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class NewRecruitmentCommentChildLikeAlarmCreator extends AlarmCreator {
    @Builder
    public NewRecruitmentCommentChildLikeAlarmCreator(
            NewCommentChildLikeAlarmInfo newCommentChildLikeAlarmInfo
    ) {
        this.type = AlarmType.NEW_RECRUITMENT_COMMENT_CHILD_LIKE;
        this.targetUserId = newCommentChildLikeAlarmInfo.getTargetUserId();
        this.title = newCommentChildLikeAlarmInfo.getTitle();
        this.message = newCommentChildLikeAlarmInfo.getMessage();
        this.time = newCommentChildLikeAlarmInfo.getTime();

        Subject subjectObject = Subject.builder()
                .commentId(newCommentChildLikeAlarmInfo.getCommentId())
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
