package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostDetailInfo;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request.PostModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request.PostRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostSimpleInfo;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.global.exception.NoPermissionException;

public interface PostService {
    PostSimpleInfo getPostSimpleInfo(Long postId);
    PostDetailInfo getPostDetailInfo(Long postId);
    Post registerPost(User user, PostRegisterRequest postRegisterRequest);
    void modifyPost(Post post, PostModifyRequest postModifyRequest);
    void deletePost(Post post);
    void addLike(Post post, User user) throws DuplicateLikeException;
    void deleteLike(Post post, User user) throws LikeNotExistException;
    boolean existsLike(Post post, User user);

    void checkWritePermission(Post post, User user) throws NoPermissionException;
}
