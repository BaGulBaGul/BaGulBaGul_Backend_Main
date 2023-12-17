package com.BaGulBaGul.BaGulBaGul.domain.post.event;

import com.BaGulBaGul.BaGulBaGul.global.event.BasicTimeEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPostCommentChildLikeEvent extends BasicTimeEvent {
    //좋아요를 받은 대댓글의 id
    private Long postCommentChildId;
    //좋아요 누른 유저의 id
    private Long userId;

    public NewPostCommentChildLikeEvent(Long postCommentChildId, Long userId) {
        this.postCommentChildId = postCommentChildId;
        this.userId = userId;
    }
}
