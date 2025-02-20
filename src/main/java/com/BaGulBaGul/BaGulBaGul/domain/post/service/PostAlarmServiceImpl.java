package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostComment;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentChild;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentChildRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentRepository;

import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.service.creator.post.NewCommentAlarmInfo;
import com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.service.creator.post.NewCommentChildAlarmInfo;
import com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.service.creator.post.NewCommentChildLikeAlarmInfo;
import com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.service.creator.post.NewCommentLikeAlarmInfo;
import com.BaGulBaGul.BaGulBaGul.domain.alarm.dto.service.creator.post.NewPostLikeAlarmInfo;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostAlarmServiceImpl implements PostAlarmService {

    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostCommentChildRepository postCommentChildRepository;

    /*
        게시글에 좋아요 추가 시 게시글 작성자에게 알람
    */
    @Override
    @Transactional
    public NewPostLikeAlarmInfo getNewPostLikeAlarmInfo(LocalDateTime time, Long likedPostId) {
        //좋아요를 받은 게시글 조회
        Post likedPost = postRepository.findById(likedPostId).orElse(null);
        //게시글이 없을 경우
        if(likedPost == null) {
            return null;
        }
        //게시글의 작성자
        User likedPostWriter = likedPost.getUser();

        return NewPostLikeAlarmInfo.builder()
                .targetUserId(likedPostWriter.getId())
                .time(time)
                .postTitle(likedPost.getTitle())
                .likeCount(likedPost.getLikeCount())
                .build();
    }

    /*
        게시글에 댓글 추가 시 게시글 작성자에게 알람
    */
    @Override
    @Transactional
    public NewCommentAlarmInfo getNewCommentAlarmInfo(
            LocalDateTime time,
            Long newCommentId
    ) {
        //새로 등록된 댓글을 조회
        PostComment newComment = postCommentRepository
                .findById(newCommentId)
                .orElse(null);
        //새로 등록된 댓글이 없을 경우
        if(newComment == null){
             return null;
        }
        //새로 등록된 댓글이 등록된 게시글
        Post post = newComment.getPost();
        //게시글의 작성자
        User postWriter = post.getUser();

        return NewCommentAlarmInfo.builder()
                .targetUserId(postWriter.getId())
                .time(time)
                .postTitle(post.getTitle())
                .commentContent(newComment.getContent())
                .build();
    }

    /*
        댓글에 대댓글 추가 시 댓글 작성자에게 알람
    */
    @Override
    @Transactional
    public NewCommentChildAlarmInfo getNewCommentChildAlarmInfo(
            LocalDateTime time,
            Long newCommentChildId
    ) {
        //새로 등록된 대댓글을 조회
        PostCommentChild newCommentChild = postCommentChildRepository
                .findById(newCommentChildId)
                .orElse(null);
        //새로 등록된 대댓글이 없을 경우
        if(newCommentChild == null) {
            return null;
        }
        //새로 등록된 대댓글의 부모 댓글
        PostComment parentComment = newCommentChild.getPostComment();
        //부모 댓글의 작성자
        User parentCommentWriter = parentComment.getUser();

        return NewCommentChildAlarmInfo.builder()
                .targetUserId(parentCommentWriter.getId())
                .time(time)
                .commentId(parentComment.getId())
                .commentChildContent(newCommentChild.getContent())
                .build();
    }

    /*
        댓글에 좋아요 추가 시 댓글 작성자에게 알람
    */
    @Override
    @Transactional
    public NewCommentLikeAlarmInfo getNewCommentLikeAlarmInfo(
            LocalDateTime time,
            Long likedCommentId
    ) {
        //좋아요를 받은 댓글을 조회
        PostComment likedComment = postCommentRepository
                .findById(likedCommentId)
                .orElse(null);
        //댓글이 없을 경우
        if(likedComment == null) {
            return null;
        }
        //좋아요를 받은 댓글의 작성자
        User likedCommentWriter = likedComment.getUser();

        return NewCommentLikeAlarmInfo.builder()
                .targetUserId(likedCommentWriter.getId())
                .time(time)
                .commentId(likedComment.getId())
                .commentContent(likedComment.getContent())
                .commentLikeCount(likedComment.getLikeCount())
                .build();
    }

    /*
        대댓글에 답장 시 대댓글 작성자에게 알람
    */
    @Override
    @Transactional
    public NewCommentChildAlarmInfo getNewCommentChildAlarmInfoIfReply(
            LocalDateTime time,
            Long newCommentChildId,
            Long replyTargetCommentChildId
    ) {
        //새로 등록된 대댓글이 답장이 아니라면 무시
        if(replyTargetCommentChildId == null) {
            return null;
        }
        //새로 등록한 대댓글 엔티티를 조회
        PostCommentChild newCommentChild = postCommentChildRepository
                .findById(newCommentChildId)
                .orElse(null);
        //새로 등록한 대댓글이 지워진 경우
        if(newCommentChild == null) {
            return null;
        }

        //답장을 받을 대댓글 엔티티를 조회
        PostCommentChild replyTargetCommentChild = postCommentChildRepository
                .findById(replyTargetCommentChildId)
                .orElse(null);
        //답글을 받을 대댓글이 지워진 경우
        if(replyTargetCommentChild == null) {
            return null;
        }
        //답장을 받을 대댓글의 작성자 엔티티
        User replyTargetCommentChildWriter = replyTargetCommentChild.getUser();

        //부모 댓글 엔티티
        PostComment parentComment = replyTargetCommentChild.getPostComment();
        //부모 댓글의 작성자
        User parentCommentWriter = parentComment.getUser();
        //알람을 받는 유저(답장을 받을 대댓글의 작성자)가 부모 댓글의 작성자와 같다면 무시
        //부모 댓글의 작성자에게는 대댓글 작성 알람이 가기 때문에 중복 알람을 막기 위함
        if(replyTargetCommentChildWriter.getId() == parentCommentWriter.getId()) {
            return null;
        }

        return NewCommentChildAlarmInfo.builder()
                .targetUserId(replyTargetCommentChildWriter.getId())
                .time(time)
                .commentId(parentComment.getId())
                .commentChildContent(newCommentChild.getContent())
                .build();
    }

    /*
        대댓글에 좋아요 추가 시 대댓글 작성자에게 알람
    */
    @Override
    @Transactional
    public NewCommentChildLikeAlarmInfo getNewCommentChildLikeAlarmInfo(
            LocalDateTime time,
            Long likedCommentChildId
    ) {
        //좋아요를 받은 대댓글을 조회
        PostCommentChild likedCommentChild = postCommentChildRepository
                .findById(likedCommentChildId)
                .orElse(null);
        //좋아요를 받은 대댓글이 없는 경우
        if(likedCommentChild == null) {
            return null;
        }
        //좋아요를 받은 대댓글의 작성자
        User likedCommentChildWriter = likedCommentChild.getUser();
        //좋아요를 받은 대댓글의 부모 댓글
        PostComment parentComment = likedCommentChild.getPostComment();

        return NewCommentChildLikeAlarmInfo.builder()
                .targetUserId(likedCommentChildWriter.getId())
                .time(time)
                .commentId(parentComment.getId())
                .commentChildContent(likedCommentChild.getContent())
                .commentChildLikeCount(likedCommentChild.getLikeCount())
                .build();
    }
}
