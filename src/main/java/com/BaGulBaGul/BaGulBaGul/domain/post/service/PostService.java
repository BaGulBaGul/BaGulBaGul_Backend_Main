package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;

public interface PostService {
    Post registerPost(User user, PostRegisterRequest postRegisterRequest);
    void modifyPost(Post post, PostModifyRequest postModifyRequest);
    void deletePost(Post post);
    void addLike(Post post, User user) throws DuplicateLikeException;
    void deleteLike(Post post, User user) throws LikeNotExistException;
    boolean existsLike(Post post, User user);
}
