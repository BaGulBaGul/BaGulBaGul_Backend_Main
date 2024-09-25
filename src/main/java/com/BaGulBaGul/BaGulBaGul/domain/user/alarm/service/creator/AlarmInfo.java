package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import com.BaGulBaGul.BaGulBaGul.global.config.JsonConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public abstract class AlarmInfo {
    protected static final ObjectMapper objectMapper = JsonConfig.getObjectMapper();

    protected AlarmType type;
    protected Long targetUserId;
    protected String title;
    protected String message;
    protected String subject;
    protected LocalDateTime time;

    protected static String makeSubjectJSON(Object subjectObject) throws JsonProcessingException {
        return objectMapper.writeValueAsString(subjectObject);
    }
}
