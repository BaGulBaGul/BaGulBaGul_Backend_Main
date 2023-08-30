package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostComment;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentChild;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentChildRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostCommentServiceImpl implements PostCommentService {

    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostCommentChildRepository postCommentChildRepository;

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
}
