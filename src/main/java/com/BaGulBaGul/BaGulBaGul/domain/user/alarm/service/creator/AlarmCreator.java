package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public abstract class AlarmCreator {
    //생성 비용이 크지만 thread safe하므로 미리 생성해 둔다.
    protected static final ObjectMapper objectMapper = new ObjectMapper();

    protected AlarmType type;
    protected Long targetUserId;
    protected String title;
    protected String message;
    protected String subject;
    protected LocalDateTime time;
}
