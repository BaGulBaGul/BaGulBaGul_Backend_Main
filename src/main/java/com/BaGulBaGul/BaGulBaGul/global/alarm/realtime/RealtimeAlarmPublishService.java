package com.BaGulBaGul.BaGulBaGul.global.alarm.realtime;

public interface RealtimeAlarmPublishService {
    void publishAlarm(Long userId, String message);
}
