package com.BaGulBaGul.BaGulBaGul.domain.post.event;

import com.BaGulBaGul.BaGulBaGul.global.event.BasicTimeEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPostCommentChildEvent extends BasicTimeEvent {
    //새로 추가된 대댓글의 id
    private Long postCommentChildId;
    //답글일 경우 답장을 보낼 대댓글의 Id
    private Long originalPostCommentChildId;

    public NewPostCommentChildEvent(Long postCommentChildId, Long originalPostCommentChildId) {
        this.postCommentChildId = postCommentChildId;
        this.originalPostCommentChildId = originalPostCommentChildId;
    }
}
