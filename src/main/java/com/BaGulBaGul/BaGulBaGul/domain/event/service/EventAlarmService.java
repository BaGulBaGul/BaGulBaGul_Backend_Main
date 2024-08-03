package com.BaGulBaGul.BaGulBaGul.domain.event.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.applicationevent.NewEventLikeApplicationEvent;

public interface EventAlarmService {
    void alarmToEventWriter(NewEventLikeApplicationEvent newEventLikeApplicationEvent);
}
