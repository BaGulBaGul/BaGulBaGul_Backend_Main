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
public class NewEventCommentChildLikeApplicationEvent extends BasicTimeApplicationEvent {
    //좋아요를 받은 대댓글의 id
    private Long likedCommentChildId;
}
