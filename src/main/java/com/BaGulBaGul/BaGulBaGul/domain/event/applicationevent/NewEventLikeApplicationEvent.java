package com.BaGulBaGul.BaGulBaGul.domain.event.applicationevent;

import com.BaGulBaGul.BaGulBaGul.global.event.BasicTimeEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class NewEventLikeApplicationEvent extends BasicTimeEvent {
    //좋아요를 받은 이벤트의 id
    private Long eventId;
    //좋아요를 누른 유저의 id
    private Long userId;
}
