package com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent;

import com.BaGulBaGul.BaGulBaGul.global.applicationevent.BasicTimeApplicationEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPostLikeApplicationEvent extends BasicTimeApplicationEvent {
    //좋아요를 받은 게시글의 id
    private Long postId;
    //좋아요를 누른 유저의 id
    private Long userId;
    public NewPostLikeApplicationEvent(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
