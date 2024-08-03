package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent.NewPostCommentChildApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent.NewPostCommentChildLikeApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent.NewPostCommentApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent.NewPostCommentLikeApplicationEvent;

public interface PostAlarmService {
    /*
        게시글에 댓글 추가 시 게시글 작성자에게 알람
     */
    void alarmToPostWriter(NewPostCommentApplicationEvent newPostCommentApplicationEvent);
    /*
        댓글에 대댓글 추가 시 댓글 작성자에게 알람
    */
    void alarmToPostCommentWriter(NewPostCommentChildApplicationEvent newPostCommentChildApplicationEvent);
    /*
        댓글에 좋아요 추가 시 댓글 작성자에게 알람
    */
    void alarmToPostCommentWriter(NewPostCommentLikeApplicationEvent newPostCommentLikeEvent);
    /*
        대댓글에 답장 시 대댓글 작성자에게 알람
    */
    void alarmToPostCommentChildWriter(NewPostCommentChildApplicationEvent newPostCommentChildApplicationEvent);
    /*
        대댓글에 좋아요 추가 시 대댓글 작성자에게 알람
    */
    void alarmToPostCommentChildWriter(NewPostCommentChildLikeApplicationEvent newPostCommentChildLikeApplicationEvent);
}
