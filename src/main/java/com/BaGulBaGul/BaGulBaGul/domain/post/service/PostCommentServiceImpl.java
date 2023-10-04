package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostComment;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentChild;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentChildLike;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentLike;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentChildLikeRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentChildRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentLikeRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostCommentServiceImpl implements PostCommentService {

    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostCommentChildRepository postCommentChildRepository;
    private final PostCommentLikeRepository postCommentLikeRepository;
    private final PostCommentChildLikeRepository postCommentChildLikeRepository;

    @Override
    @Transactional
    public PostComment registerComment(Post post, User user, String content) {
        //먼저 w lock획득 후 s lock(FK)을 얻어야 deadlock 방지
        //댓글 수 증가
        postRepository.increaseCommentCount(post);
        PostComment postComment = PostComment.builder()
                .post(post)
                .user(user)
                .content(content)
                .build();
        //댓글 등록
        postCommentRepository.save(postComment);
        return postComment;
    }

    @Override
    @Transactional
    public void deleteComment(PostComment postComment) {
        //대댓글과 연결된 좋아요 전부 삭제
        postCommentChildLikeRepository.deleteAllByPostComment(postComment);
        //연결된 대댓글 전부 삭제
        postCommentChildRepository.deleteAllByPostComment(postComment);
        //댓글과 연결된 좋아요 전부 삭제
        postCommentLikeRepository.deleteAllByPostComment(postComment);
        //댓글 삭제
        postCommentRepository.delete(postComment);
        //게시글 댓글 개수 1 감소
        postRepository.decreaseCommentCount(postComment.getPost());
    }

    @Override
    @Transactional
    public PostCommentChild registerCommentChild(PostComment postComment, User user, String content) {
        postCommentRepository.increaseCommentChildCount(postComment);
        PostCommentChild postCommentChild = PostCommentChild.builder()
                .postComment(postComment)
                .user(user)
                .content(content)
                .build();
        postCommentChildRepository.save(postCommentChild);
        return postCommentChild;
    }

    @Override
    @Transactional
    public void deleteCommentChild(PostCommentChild postCommentChild) {
        //대댓글과 연결된 좋아요 삭제
        postCommentChildLikeRepository.deleteAllByPostCommentChild(postCommentChild);
        //대댓글 삭제
        postCommentChildRepository.delete(postCommentChild);
        //댓글의 대댓글 개수 1 감소
        postCommentRepository.decreaseCommentChildCount(postCommentChild.getPostComment());
    }

    @Override
    @Transactional(rollbackFor = {DuplicateLikeException.class})
    public void addLikeToComment(PostComment postComment, User user) throws DuplicateLikeException {
        //먼저 w lock 획득 후 s lock(FK) 획득
        postCommentRepository.increaseLikeCount(postComment);
        try {
            postCommentLikeRepository.save(new PostCommentLike(postComment, user));
            postCommentLikeRepository.flush();
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new DuplicateLikeException();
        }
    }

    @Override
    @Transactional(rollbackFor = LikeNotExistException.class)
    public void deleteLikeToComment(PostComment postComment, User user) throws LikeNotExistException {
        postCommentRepository.decreaseCommentChildCount(postComment);
        int deletedCnt = postCommentLikeRepository.deleteAndGetCountByPostCommentAndUser(postComment, user);
        if(deletedCnt == 0) {
            throw new LikeNotExistException();
        }
    }

    @Override
    @Transactional(rollbackFor = {DuplicateLikeException.class})
    public void addLikeToCommentChild(PostCommentChild postCommentChild, User user) throws DuplicateLikeException {
        postCommentChildRepository.increaseLikeCount(postCommentChild);
        try {
            postCommentChildLikeRepository.save(new PostCommentChildLike(postCommentChild, user));
            postCommentChildLikeRepository.flush();
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new DuplicateLikeException();
        }
    }

    @Override
    @Transactional(rollbackFor = LikeNotExistException.class)
    public void deleteLikeToCommentChild(PostCommentChild postCommentChild, User user) throws LikeNotExistException {
        postCommentChildRepository.decreaseLikeCount(postCommentChild);
        int deletedCnt = postCommentChildLikeRepository.deleteAndGetCountByPostCommentChildAndUser(postCommentChild, user);
        if(deletedCnt == 0) {
            throw new LikeNotExistException();
        }
    }
}
