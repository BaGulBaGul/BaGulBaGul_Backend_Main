package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class NewCommentAlarmCreator extends AlarmCreator {

    private static final String titleFormat = "%s 글에 댓글이 달렸어요";

    @Builder
    public NewCommentAlarmCreator(
            Long targetUserId,
            LocalDateTime time,
            Long postId,
            String postTitle,
            String commentContent
    ) {
        this.type = AlarmType.NEW_COMMENT;
        this.time = time;
        this.targetUserId = targetUserId;
        this.title = makeAlarmTitle(postTitle);
        this.message = commentContent;
        try {
            this.subject = makeSubjectJSON(postId);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("AlarmCreator subject json 변환 실패");
        }
    }

    private String makeAlarmTitle(String postTitle) {
        return String.format(titleFormat, postTitle);
    }
    private String makeSubjectJSON(Long postId) throws JsonProcessingException {
        Subject subject = Subject.builder()
                .postId(postId)
                .build();
        return super.objectMapper.writeValueAsString(subject);
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    private static class Subject {
        Long postId;
    }
}
