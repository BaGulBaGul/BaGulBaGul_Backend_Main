package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.event.NewPostCommentChildEvent;
import com.BaGulBaGul.BaGulBaGul.domain.post.event.NewPostCommentChildLikeEvent;
import com.BaGulBaGul.BaGulBaGul.domain.post.event.NewPostCommentEvent;
import com.BaGulBaGul.BaGulBaGul.domain.post.event.NewPostCommentLikeEvent;
import com.BaGulBaGul.BaGulBaGul.domain.post.event.NewPostLikeEvent;

public interface PostAlarmService {
    /*
        게시글에 댓글 추가 시 게시글 작성자에게 알람
     */
    void alarmToPostWriter(NewPostCommentEvent newPostCommentEvent);
    /*
        게시글에 좋아요 추가 시 게시글 작성자에게 알람
    */
    void alarmToPostWriter(NewPostLikeEvent newPostLikeEvent);
    /*
        댓글에 대댓글 추가 시 댓글 작성자에게 알람
    */
    void alarmToPostCommentWriter(NewPostCommentChildEvent newPostCommentChildEvent);
    /*
        댓글에 좋아요 추가 시 댓글 작성자에게 알람
    */
    void alarmToPostCommentWriter(NewPostCommentLikeEvent newPostCommentLikeEvent);
    /*
        대댓글에 답장 시 대댓글 작성자에게 알람
    */
    void alarmToPostCommentChildWriter(NewPostCommentChildEvent newPostCommentChildEvent);
    /*
        대댓글에 좋아요 추가 시 대댓글 작성자에게 알람
    */
    void alarmToPostCommentChildWriter(NewPostCommentChildLikeEvent newPostCommentChildLikeEvent);
}
