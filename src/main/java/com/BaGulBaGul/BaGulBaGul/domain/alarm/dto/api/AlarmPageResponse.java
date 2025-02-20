package com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.api;

import com.BaGulBaGul.BaGulBaGul.domain.alarm.constant.AlarmType;
import com.BaGulBaGul.BaGulBaGul.domain.alarm.Alarm;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class AlarmPageResponse {
    private Long alarmId;
    private AlarmType type;
    private String title;
    private String message;
    private String subject;
    private boolean checked;
    private LocalDateTime time;

    public static AlarmPageResponse of(Alarm alarm) {
        return AlarmPageResponse.builder()
                .alarmId(alarm.getId())
                .type(alarm.getType())
                .title(alarm.getTitle())
                .message(alarm.getMessage())
                .subject(alarm.getSubject())
                .checked(alarm.isChecked())
                .time(alarm.getTime())
                .build();
    }
}
