package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostComment;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentChild;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;

public interface PostCommentService {
    PostComment registerComment(Post post, User user, String content);
    void deleteComment(PostComment postComment);
    PostCommentChild registerCommentChild(PostComment postComment, User user, String content);
}
