package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class NewCommentChildLikeAlarmCreator extends AlarmCreator {
    private static final String titleFormat = "작성하신 댓글에 좋아요 %d개가 눌렸어요";

    @Builder
    public NewCommentChildLikeAlarmCreator(
            Long targetUserId,
            LocalDateTime time,
            Long commentId,
            String commentChildContent,
            int commentChildLikeCount
    ) {
        this.type = AlarmType.NEW_COMMENT_CHILD_LIKE;
        this.time = time;
        this.targetUserId = targetUserId;
        this.title = makeAlarmTitle(commentChildLikeCount);
        this.message = commentChildContent;
        try {
            this.subject = makeSubjectJSON(commentId);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("AlarmCreator subject json 변환 실패");
        }
    }

    private String makeAlarmTitle(int likeCount) {
        return String.format(titleFormat, likeCount);
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
