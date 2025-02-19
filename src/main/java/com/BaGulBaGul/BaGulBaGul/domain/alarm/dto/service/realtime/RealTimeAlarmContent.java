package com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.service.realtime;

import com.BaGulBaGul.BaGulBaGul.domain.alarm.constant.AlarmType;
import com.BaGulBaGul.BaGulBaGul.domain.alarm.Alarm;
import com.BaGulBaGul.BaGulBaGul.global.config.JsonConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RealTimeAlarmContent {
    private static final ObjectMapper objectMapper = JsonConfig.getObjectMapper();

    private Long alarmId;
    private AlarmType type;
    private String title;
    private String message;
    private String subject;
    private LocalDateTime time;

    public static RealTimeAlarmContent from(Alarm alarm) {
        return RealTimeAlarmContent.builder()
                .alarmId(alarm.getId())
                .type(alarm.getType())
                .title(alarm.getTitle())
                .message(alarm.getMessage())
                .subject(alarm.getSubject())
                .time(alarm.getTime())
                .build();
    }

    public String toJSON() throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }
}
