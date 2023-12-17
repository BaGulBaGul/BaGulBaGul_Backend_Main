package com.BaGulBaGul.BaGulBaGul.domain.post.event;

import com.BaGulBaGul.BaGulBaGul.global.event.BasicTimeEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPostCommentEvent extends BasicTimeEvent {
    //새로 추가된 댓글의 id
    private Long postCommentId;
    public NewPostCommentEvent(Long postCommentId) {
        this.postCommentId = postCommentId;
    }
}
