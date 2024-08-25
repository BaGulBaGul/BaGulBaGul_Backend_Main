package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostComment;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentChild;
import com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent.NewPostCommentChildApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent.NewPostCommentChildLikeApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent.NewPostCommentApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent.NewPostCommentLikeApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentChildRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentRepository;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.AlarmService;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.AlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.post.NewCommentAlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.post.NewCommentChildAlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.post.NewCommentChildLikeAlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.post.NewCommentLikeAlarmCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class PostAlarmServiceImpl implements PostAlarmService {

    private final PostCommentRepository postCommentRepository;
    private final PostCommentChildRepository postCommentChildRepository;
    private final AlarmService alarmService;

    /*
        게시글에 댓글 추가 시 게시글 작성자에게 알람
     */
    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async
    public void alarmToPostWriter(
            NewPostCommentApplicationEvent newPostCommentApplicationEvent
    ) {
        PostComment postComment = postCommentRepository
                .findById(newPostCommentApplicationEvent.getPostCommentId())
                .orElse(null);
        if(postComment == null){
             return;
        }
        Post post = postComment.getPost();
        AlarmCreator alarmCreator = NewCommentAlarmCreator.builder()
                .targetUserId(post.getUser().getId())
                .time(newPostCommentApplicationEvent.getTime())
                .postId(post.getId())
                .postTitle(post.getTitle())
                .commentContent(postComment.getContent())
                .build();
        //알람 등록
        alarmService.registerAlarm(alarmCreator);
    }

    /*
        댓글에 대댓글 추가 시 댓글 작성자에게 알람
    */
    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async
    public void alarmToPostCommentWriter(
            NewPostCommentChildApplicationEvent newPostCommentChildApplicationEvent
    ) {
        PostCommentChild newPostCommentChild = postCommentChildRepository
                .findById(newPostCommentChildApplicationEvent.getPostCommentChildId())
                .orElse(null);
        if(newPostCommentChild == null) {
            return;
        }
        PostComment postComment = newPostCommentChild.getPostComment();

        AlarmCreator alarmCreator = NewCommentChildAlarmCreator.builder()
                .targetUserId(postComment.getUser().getId())
                .time(newPostCommentChildApplicationEvent.getTime())
                .commentId(postComment.getId())
                .commentChildContent(newPostCommentChild.getContent())
                .build();
        //알람 등록
        alarmService.registerAlarm(alarmCreator);
    }

    /*
        댓글에 좋아요 추가 시 댓글 작성자에게 알람
    */
    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async
    public void alarmToPostCommentWriter(
            NewPostCommentLikeApplicationEvent newPostCommentLikeEvent
    ) {
        PostComment postComment = postCommentRepository
                .findById(newPostCommentLikeEvent.getPostCommentId())
                .orElse(null);
        if(postComment == null) {
            return;
        }

        AlarmCreator alarmCreator = NewCommentLikeAlarmCreator.builder()
                .targetUserId(postComment.getUser().getId())
                .time(newPostCommentLikeEvent.getTime())
                .commentId(postComment.getId())
                .commentContent(postComment.getContent())
                .commentLikeCount(postComment.getLikeCount())
                .build();
        //알람 등록
        alarmService.registerAlarm(alarmCreator);
    }

    /*
        대댓글에 답장 시 대댓글 작성자에게 알람
    */
    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async
    public void alarmToPostCommentChildWriter(
            NewPostCommentChildApplicationEvent newPostCommentChildApplicationEvent
    ) {
        //새로 등록된 대댓글이 답장이 아니라면 무시
        if(newPostCommentChildApplicationEvent.getOriginalPostCommentChildId() == null) {
            return;
        }

        PostCommentChild newPostCommentChild = postCommentChildRepository
                .findById(newPostCommentChildApplicationEvent.getPostCommentChildId())
                .orElse(null);
        //등록된 대댓글이 지워졌거나 답글이 아닌 경우
        if(newPostCommentChild == null) {
            return;
        }

        PostCommentChild originalPostCommentChild = postCommentChildRepository
                .findById(newPostCommentChildApplicationEvent.getOriginalPostCommentChildId())
                .orElse(null);
        //답글을 받을 대댓글이 지워진 경우
        if(originalPostCommentChild == null) {
            return;
        }

        PostComment postComment = originalPostCommentChild.getPostComment();
        //알람을 받는 유저가 부모 댓글의 작성자와 같다면 무시
        if(originalPostCommentChild.getUser().getId() == postComment.getUser().getId()) {
            return;
        }

        AlarmCreator alarmCreator = NewCommentChildAlarmCreator.builder()
                .targetUserId(originalPostCommentChild.getUser().getId())
                .time(newPostCommentChildApplicationEvent.getTime())
                .commentId(postComment.getId())
                .commentChildContent(newPostCommentChild.getContent())
                .build();
        //알람 등록
        alarmService.registerAlarm(alarmCreator);
    }

    /*
        대댓글에 좋아요 추가 시 대댓글 작성자에게 알람
    */
    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async
    public void alarmToPostCommentChildWriter(
            NewPostCommentChildLikeApplicationEvent newPostCommentChildLikeApplicationEvent
    ) {
        PostCommentChild postCommentChild = postCommentChildRepository
                .findById(newPostCommentChildLikeApplicationEvent.getPostCommentChildId())
                .orElse(null);
        if(postCommentChild == null) {
            return;
        }
        PostComment postComment = postCommentChild.getPostComment();

        AlarmCreator alarmCreator = NewCommentChildLikeAlarmCreator.builder()
                .targetUserId(postCommentChild.getUser().getId())
                .time(newPostCommentChildLikeApplicationEvent.getTime())
                .commentId(postComment.getId())
                .commentChildContent(postCommentChild.getContent())
                .commentChildLikeCount(postCommentChild.getLikeCount())
                .build();
        //알람 등록
        alarmService.registerAlarm(alarmCreator);
    }
}
