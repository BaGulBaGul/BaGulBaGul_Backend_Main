package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public abstract class AlarmCreator {
    protected AlarmType type;
    protected Long targetUserId;
    protected String title;
    protected String message;
    protected String subject;
    protected LocalDateTime time;
}
