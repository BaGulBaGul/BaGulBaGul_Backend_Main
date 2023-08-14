package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import com.BaGulBaGul.BaGulBaGul.global.response.ErrorCode;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostAPIServiceImpl implements PostAPIService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostService postService;

    @Transactional
    public PostDetailResponse getPostDetailById(Long postId) {
        Post post = postRepository.findWithUserAndCategoriesById(postId).orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        return PostDetailResponse.of(post);
    }

    @Override
    public Page<PostSimpleResponse> getPostPageByCondition(PostConditionalRequest postConditionalRequest, Pageable pageable) {
        return postRepository.findPostSimpleResponsePageByCondition(postConditionalRequest, pageable);
    }

    @Override
    @Transactional
    public Long registerPost(Long userId, PostRegisterRequest postRegisterRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        //post 생성
        Post post = Post.builder()
                .type(postRegisterRequest.getType())
                .user(user)
                .title(postRegisterRequest.getTitle())
                .content(postRegisterRequest.getContent())
                .headCount(postRegisterRequest.getHeadCount())
                .startDate(postRegisterRequest.getStartDate())
                .endDate(postRegisterRequest.getEndDate())
                .tags(postRegisterRequest.getTags().stream().collect(Collectors.joining(" ")))
                .image_url(postRegisterRequest.getImage_url())
                .likeCount(0)
                .commentCount(0)
                .build();
        //카테고리 등록
        for(String categoryName : postRegisterRequest.getCategories()) {
            postService.addCategory(post, categoryName);
        }
        //post 생성
        postRepository.save(post);
        return post.getId();
    }

    @Override
    @Transactional
    public void modifyPost(Long postId, Long userId, PostModifyRequest postModifyRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        Post post = postRepository.findById(postId).orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        //요청한 유저가 작성자가 아닐 경우 수정 권한 없음
        if(post.getUser().getId() != userId) {
            throw new GeneralException(ErrorCode.FORBIDDEN);
        }
        //patch 방식으로 postModifyRequest에서 null이 아닌 모든 필드를 변경
        if(postModifyRequest.getType() != null) {
            post.setType(postModifyRequest.getType());
        }
        if(postModifyRequest.getTitle() != null && !postModifyRequest.getTitle().isEmpty()) {
            post.setTitle(postModifyRequest.getTitle());
        }
        if(postModifyRequest.getHeadCount() != null) {
            post.setHeadCount(postModifyRequest.getHeadCount());
        }
        if(postModifyRequest.getContent() != null) {
            post.setContent(postModifyRequest.getContent());
        }
        if(postModifyRequest.getStartDate() != null) {
            post.setStartDate(postModifyRequest.getStartDate());
        }
        if(postModifyRequest.getEndDate() != null) {
            post.setEndDate(postModifyRequest.getEndDate());
        }
        if(postModifyRequest.getTags() != null) {
            post.setTags(postModifyRequest.getTags().stream().collect(Collectors.joining(" ")));
        }
        if(postModifyRequest.getCategories() != null) {
            //카테고리 초기화
            postService.clearCategory(post);
            //수정 요청한 카테고리를 전부 추가
            for(String categoryName : postModifyRequest.getCategories()) {
                postService.addCategory(post, categoryName);
            }
        }
        if(postModifyRequest.getImage_url() != null) {
            post.setImage_url(postModifyRequest.getImage_url());
        }
    }

    @Override
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        //요청한 유저가 작성자가 아닐 경우 수정 권한 없음
        if(post.getUser().getId() != userId) {
            throw new GeneralException(ErrorCode.FORBIDDEN);
        }
        //삭제
        postService.delete(post);
    }

    @Override
    @Transactional(rollbackFor = {DuplicateLikeException.class})
    public void addLike(Long postId, Long userId) throws DuplicateLikeException {
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        Post post = postRepository.findById(postId).orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        //addLike에서 db단의 검증이 수행되지만 애플리케이션에서도 한번 검사
        if(postService.existsLike(post, user)) {
            throw new DuplicateLikeException();
        }
        postService.addLike(post, user);
    }

    @Override
    @Transactional(rollbackFor = {LikeNotExistException.class})
    public void deleteLike(Long postId, Long userId) throws LikeNotExistException {
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        Post post = postRepository.findById(postId).orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        if(!postService.existsLike(post, user)) {
            throw new LikeNotExistException();
        }
        postService.deleteLike(post, user);
    }

    @Override
    @Transactional
    public boolean isMyLike(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        Post post = postRepository.findById(postId).orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        return postService.existsLike(post, user);
    }
}