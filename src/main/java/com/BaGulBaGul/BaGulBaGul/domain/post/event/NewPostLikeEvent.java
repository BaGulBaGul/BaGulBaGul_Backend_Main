package com.BaGulBaGul.BaGulBaGul.domain.post.event;

import com.BaGulBaGul.BaGulBaGul.global.event.BasicTimeEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPostLikeEvent extends BasicTimeEvent {
    //좋아요를 받은 게시글의 id
    private Long postId;
    //좋아요를 누른 유저의 id
    private Long userId;
    public NewPostLikeEvent(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
