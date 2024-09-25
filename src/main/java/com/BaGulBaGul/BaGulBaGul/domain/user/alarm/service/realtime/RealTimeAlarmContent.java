package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.realtime;

import com.BaGulBaGul.BaGulBaGul.domain.user.Alarm;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import com.BaGulBaGul.BaGulBaGul.global.config.JsonConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private AlarmType type;
    private String title;
    private String message;
    private String subject;
    private LocalDateTime time;

    public static RealTimeAlarmContent from(Alarm alarm) {
        return RealTimeAlarmContent.builder()
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
