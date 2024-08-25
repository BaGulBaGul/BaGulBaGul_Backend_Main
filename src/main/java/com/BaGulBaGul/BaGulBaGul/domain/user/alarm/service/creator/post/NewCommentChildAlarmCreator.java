package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.post;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.AlarmCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class NewCommentChildAlarmCreator extends AlarmCreator {

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
        try {
            this.subject = makeSubjectJSON(commentId);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("AlarmCreator subject json 변환 실패");
        }
    }

    private String makeSubjectJSON(Long commentId) throws JsonProcessingException {
        Subject subject = Subject.builder()
                .commentId(commentId)
                .build();
        return super.objectMapper.writeValueAsString(subject);
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    private static class Subject {
        Long commentId;
    }
}