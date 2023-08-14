package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Category;
import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCategory;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostLike;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.CategoryNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.CategoryRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCategoryRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentChildLikeRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentChildRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentLikeRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostLikeRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostCategoryRepository postCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostCommentLikeRepository postCommentLikeRepository;
    private final PostCommentChildRepository postCommentChildRepository;
    private final PostCommentChildLikeRepository postCommentChildLikeRepository;

    @Override
    @Transactional
    public void clearCategory(Post post) {
        postCategoryRepository.deleteAll(post.getCategories());
        post.getCategories().clear();
        postCategoryRepository.flush();
    }

    @Override
    @Transactional
    public void addCategory(Post post, String categoryName) {
        Category category = categoryRepository.findByName(categoryName).orElseThrow(CategoryNotFoundException::new);
        this.addCategory(post, category);
    }

    @Override
    @Transactional
    public void addCategory(Post post, Category category) {
        PostCategory postCategory = new PostCategory(post, category);
        post.getCategories().add(postCategory);
    }

    @Override
    @Transactional(rollbackFor = {DuplicateLikeException.class})
    public void addLike(Post post, User user) throws DuplicateLikeException {
        //PostLike에 insert하는 것은 FK가 참조하는 Post에 share lock을 거는데 likeCount의 update는 Post에 write lock을 거므로
        //먼저 likeCount를 증가시키지 않으면 deadlock이 일어날 수 있음.
        //아니면 FK 제약조건을 해제하는 것도 고려
        postRepository.increaseLikeCount(post);
        try{
            postLikeRepository.save(new PostLike(post, user));
            postLikeRepository.flush();
        }
        catch (DataIntegrityViolationException dataIntegrityViolationException) {
            //unique 제약조건 실패 = 이미 좋아요 존재 따라서 rollback
            throw new DuplicateLikeException();
        }
    }

    @Override
    @Transactional(rollbackFor = {LikeNotExistException.class})
    public void deleteLike(Post post, User user) throws LikeNotExistException {
        //add와 마찬가지 이유로 먼저 write lock을 획득
        postRepository.decreaseLikeCount(post);
        //요청한 PostLike를 지움
        int deletedCount = postLikeRepository.deleteAndGetCountByPostAndUser(post, user);
        //지운 행의 개수가 0 = 좋아요가 없었다 따라서 예외 던지고 rollback
        if(deletedCount == 0) {
            throw new LikeNotExistException();
        }
    }

    @Override
    public boolean existsLike(Post post, User user) {
        return postLikeRepository.existsByPostAndUser(post, user);
    }

    @Override
    @Transactional
    public void delete(Post post) {
        postCategoryRepository.deleteAllByPost(post);
        postLikeRepository.deleteAllByPost(post);
        postCommentChildLikeRepository.deleteAllByPost(post);
        postCommentChildRepository.deleteAllByPost(post);
        postCommentLikeRepository.deleteAllByPost(post);
        postCommentRepository.deleteAllByPost(post);
        postRepository.delete(post);
    }
}
