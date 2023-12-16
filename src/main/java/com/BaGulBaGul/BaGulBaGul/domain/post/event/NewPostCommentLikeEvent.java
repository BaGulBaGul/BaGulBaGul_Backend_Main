package com.BaGulBaGul.BaGulBaGul.domain.post.event;

import com.BaGulBaGul.BaGulBaGul.global.event.BasicTimeEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPostCommentLikeEvent extends BasicTimeEvent {
    //좋아요를 받은 댓글의 id
    private Long postCommentId;
    //좋아요를 누른 유저의 id
    private Long userId;
    public NewPostCommentLikeEvent(Long postCommentId, Long userId) {
        this.postCommentId = postCommentId;
        this.userId = userId;
    }
}
