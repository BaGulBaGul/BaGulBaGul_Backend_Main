package com.BaGulBaGul.BaGulBaGul.global.applicationevent;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicTimeApplicationEvent {
    private LocalDateTime time;
    public BasicTimeApplicationEvent() {
        this.time = LocalDateTime.now();
    }
    public BasicTimeApplicationEvent(LocalDateTime time) {
        this.time = time;
    }
}
