package com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent;

import com.BaGulBaGul.BaGulBaGul.global.applicationevent.BasicTimeApplicationEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPostCommentApplicationEvent extends BasicTimeApplicationEvent {
    //새로 추가된 댓글의 id
    private Long postCommentId;
    public NewPostCommentApplicationEvent(Long postCommentId) {
        this.postCommentId = postCommentId;
    }
}
