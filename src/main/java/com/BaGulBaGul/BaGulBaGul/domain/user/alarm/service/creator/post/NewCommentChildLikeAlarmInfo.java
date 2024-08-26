package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.post;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.AlarmInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class NewCommentChildLikeAlarmInfo extends AlarmInfo {
    private static final String titleFormat = "작성하신 댓글에 좋아요 %d개가 눌렸어요";

    private Subject subjectObject;

    @Builder
    public NewCommentChildLikeAlarmInfo(
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
        this.subjectObject = Subject.builder()
                .commentId(commentId)
                .build();
        try {
            this.subject = makeSubjectJSON(subjectObject);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("AlarmCreator subject json 변환 실패");
        }
    }

    private String makeAlarmTitle(int likeCount) {
        return String.format(titleFormat, likeCount);
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class Subject {
        Long commentId;
    }
}
