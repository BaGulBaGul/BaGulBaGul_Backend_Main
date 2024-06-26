package com.BaGulBaGul.BaGulBaGul.global.event;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicTimeEvent {
    private LocalDateTime time;
    public BasicTimeEvent() {
        this.time = LocalDateTime.now();
    }
    public BasicTimeEvent(LocalDateTime time) {
        this.time = time;
    }
}
