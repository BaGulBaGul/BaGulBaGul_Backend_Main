package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.post.NewCommentAlarmInfo;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.post.NewCommentChildAlarmInfo;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.post.NewCommentChildLikeAlarmInfo;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.post.NewCommentLikeAlarmInfo;
import java.time.LocalDateTime;

public interface PostAlarmService {
    /*
        게시글에 댓글 추가 시 게시글 작성자에게 알람
     */
    NewCommentAlarmInfo getNewCommentAlarmInfo(LocalDateTime time, Long newCommentId);
    /*
        댓글에 대댓글 추가 시 댓글 작성자에게 알람
    */
    NewCommentChildAlarmInfo getNewCommentChildAlarmInfo(LocalDateTime time, Long newCommentChildId);
    /*
        댓글에 좋아요 추가 시 댓글 작성자에게 알람
    */
    NewCommentLikeAlarmInfo getNewCommentLikeAlarmInfo(LocalDateTime time, Long likedCommentId, Long likeUserId);
    /*
        대댓글에 답장 시 대댓글 작성자에게 알람
    */
    NewCommentChildAlarmInfo getNewCommentChildAlarmInfoIfReply(LocalDateTime time, Long newCommentChildId, Long replyTargetCommentChildId);
    /*
        대댓글에 좋아요 추가 시 대댓글 작성자에게 알람
    */
    NewCommentChildLikeAlarmInfo getNewCommentChildLikeAlarmInfo(LocalDateTime time, Long likedCommentChildId, Long likeUserId);
}
