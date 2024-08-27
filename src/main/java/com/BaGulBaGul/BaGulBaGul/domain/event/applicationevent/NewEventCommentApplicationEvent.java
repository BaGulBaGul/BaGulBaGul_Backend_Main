package com.BaGulBaGul.BaGulBaGul.domain.event.applicationevent;

import com.BaGulBaGul.BaGulBaGul.global.applicationevent.BasicTimeApplicationEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class NewEventCommentApplicationEvent extends BasicTimeApplicationEvent {
    //새로 등록된 댓글이 속한 이벤트의 id
    private Long eventId;
    //새로 등록된 댓글의 id
    private Long newCommentId;
}
