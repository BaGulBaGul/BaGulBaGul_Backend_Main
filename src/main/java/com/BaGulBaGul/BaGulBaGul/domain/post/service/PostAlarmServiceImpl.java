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
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.AlarmService;
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
    public void alarmToPostWriter(NewPostCommentApplicationEvent newPostCommentApplicationEvent) {
        PostComment postComment = postCommentRepository
                .findById(newPostCommentApplicationEvent.getPostCommentId())
                .orElse(null);
        if(postComment == null){
             return;
        }
        Post post = postComment.getPost();

        //알람 대상 = 게시글 작성자
        User targetUser = post.getUser();
        //타입
        AlarmType type = AlarmType.NEW_COMMENT;
        //제목
        String title = getNewCommentAlarmTitle(post.getTitle());
        //메세지 = 댓글 내용
        String message = postComment.getContent();
        //참조 대상 = 게시글 id
        String subjectId = post.getId().toString();
        //알람 등록
        alarmService.registerAlarm(targetUser, type, title, message, subjectId, newPostCommentApplicationEvent.getTime());
    }

    /*
        댓글에 대댓글 추가 시 댓글 작성자에게 알람
    */
    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async
    public void alarmToPostCommentWriter(NewPostCommentChildApplicationEvent newPostCommentChildApplicationEvent) {
        PostCommentChild newPostCommentChild = postCommentChildRepository
                .findById(newPostCommentChildApplicationEvent.getPostCommentChildId())
                .orElse(null);
        if(newPostCommentChild == null) {
            return;
        }
        PostComment postComment = newPostCommentChild.getPostComment();

        //알람 대상 = 부모 댓글 작성자
        User targetUser = postComment.getUser();
        //타입
        AlarmType type = AlarmType.NEW_COMMENT_CHILD;
        //제목
        String title = getNewCommentChildAlarmTitle();
        //메세지 = 대댓글 내용
        String message = newPostCommentChild.getContent();
        //참조 대상 = 부모 댓글 id
        String subjectId = postComment.getId().toString();
        //알람 등록
        alarmService.registerAlarm(targetUser, type, title, message, subjectId, newPostCommentChildApplicationEvent.getTime());
    }

    /*
        댓글에 좋아요 추가 시 댓글 작성자에게 알람
    */
    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async
    public void alarmToPostCommentWriter(NewPostCommentLikeApplicationEvent newPostCommentLikeEvent) {
        PostComment postComment = postCommentRepository
                .findById(newPostCommentLikeEvent.getPostCommentId())
                .orElse(null);
        if(postComment == null) {
            return;
        }

        //알람 대상 = 댓글 작성자
        User targetUser = postComment.getUser();
        //타입
        AlarmType type = AlarmType.NEW_COMMENT_LIKE;
        //제목
        String title = getNewPostCommentLikeAlarmTitle(postComment.getLikeCount());
        //메세지 = 댓글 내용
        String message = postComment.getContent();
        //참조 대상 = 댓글 id
        String subjectId = postComment.getId().toString();
        //알람 등록
        alarmService.registerAlarm(targetUser, type, title, message, subjectId, newPostCommentLikeEvent.getTime());
    }

    /*
        대댓글에 답장 시 대댓글 작성자에게 알람
    */
    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async
    public void alarmToPostCommentChildWriter(NewPostCommentChildApplicationEvent newPostCommentChildApplicationEvent) {
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

        //알림 대상 = originalPostCommentChild의 작성자
        User targetUser = originalPostCommentChild.getUser();
        //타입
        AlarmType type = AlarmType.NEW_COMMENT_CHILD;
        //제목
        String title = getNewCommentChildAlarmTitle();
        //메세지 = 답장 내용
        String message = newPostCommentChild.getContent();
        //참조 대상 = 부모 댓글 id
        String subjectId = postComment.getId().toString();
        //알람 등록
        alarmService.registerAlarm(targetUser, type, title, message, subjectId, newPostCommentChildApplicationEvent.getTime());
    }

    /*
        대댓글에 좋아요 추가 시 대댓글 작성자에게 알람
    */
    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async
    public void alarmToPostCommentChildWriter(
            NewPostCommentChildLikeApplicationEvent newPostCommentChildLikeApplicationEvent) {
        PostCommentChild postCommentChild = postCommentChildRepository
                .findById(newPostCommentChildLikeApplicationEvent.getPostCommentChildId())
                .orElse(null);
        if(postCommentChild == null) {
            return;
        }
        PostComment postComment = postCommentChild.getPostComment();

        //알람 대상 = 대댓글 작성자
        User targetUser = postCommentChild.getUser();
        //타입
        AlarmType type = AlarmType.NEW_COMMENT_CHILD_LIKE;
        //제목
        String title = getNewPostCommentChildLikeAlarmTitle(postCommentChild.getLikeCount());
        //메세지 = 대댓글 내용
        String message = postCommentChild.getContent();
        //참조 대상 = 부모 댓글 id
        String subjectId = postComment.getId().toString();
        //알람 등록
        alarmService.registerAlarm(targetUser, type, title, message, subjectId, newPostCommentChildLikeApplicationEvent.getTime());
    }

    private String getNewCommentAlarmTitle(String postTitle) {
        return new StringBuilder().append(postTitle).append(" 글에 댓글이 달렸어요").toString();
    }
    private String getNewCommentChildAlarmTitle() {
        return "작성하신 댓글에 답글이 달렸어요";
    }
    private String getNewPostCommentLikeAlarmTitle(int likeCount) {
        return new StringBuilder().append("작성하신 댓글에 좋아요 ").append(likeCount).append("개가 눌렸어요").toString();
    }
    private String getNewPostCommentChildLikeAlarmTitle(int likeCount) {
        return getNewPostCommentLikeAlarmTitle(likeCount);
    }
}
