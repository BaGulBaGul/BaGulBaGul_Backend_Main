package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostSimpleResponse;
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
}
