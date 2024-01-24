package com.BaGulBaGul.BaGulBaGul.global.alarm.realtime;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisRealtimeAlarmPublishService implements RealtimeAlarmPublishService {

    private final RedisTemplate<String, String> redisTemplate;
    private final String TOPIC_PREFIX = "alarm_user_";

    @Override
    public void publishAlarm(Long userId, String message) {
        redisTemplate.convertAndSend(getTopicByUserId(userId), message);
    }

    private String getTopicByUserId(Long userId) {
        return TOPIC_PREFIX + userId;
    }
}
