package com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent;

import com.BaGulBaGul.BaGulBaGul.global.applicationevent.BasicTimeApplicationEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPostCommentLikeApplicationEvent extends BasicTimeApplicationEvent {
    //좋아요를 받은 댓글의 id
    private Long postCommentId;
    //좋아요를 누른 유저의 id
    private Long userId;
    public NewPostCommentLikeApplicationEvent(Long postCommentId, Long userId) {
        this.postCommentId = postCommentId;
        this.userId = userId;
    }
}
