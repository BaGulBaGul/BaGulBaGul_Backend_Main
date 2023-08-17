package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostComment;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostCommentServiceImpl implements PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
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
}
