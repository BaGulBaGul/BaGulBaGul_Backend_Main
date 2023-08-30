package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostComment;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentChild;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetPostCommentChildPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetPostCommentPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentChildModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentChildRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentChildRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import com.BaGulBaGul.BaGulBaGul.global.response.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostCommentAPIServiceImpl implements PostCommentAPIService {

    private final PostCommentRepository postCommentRepository;
    private final PostCommentChildRepository postCommentChildRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final PostCommentService postCommentService;

    @Override
    public Page<GetPostCommentPageResponse> getPostCommentPage(Long postId, Long requestUserId, Pageable pageable) {
        //repo에서 Page를 바로 받아오면 count query에서는 requestUserId를 쓰지 않아서 Could not locate named parameter예외 발생
        List<GetPostCommentPageResponse> result;
        //requestUserId가 null이라면 left outer join이 필요 없음
        if(requestUserId == null) {
            result = postCommentRepository.getPostCommentPage(postId, pageable);
        }
        else {
            result = postCommentRepository.getPostCommentPageWithMyLike(postId, requestUserId, pageable);
        }
        Long count = postCommentRepository.getPostCommentPageWithMyLikeCount(postId);
        return new PageImpl(result, pageable, count);
    }

    @Override
    public Page<GetPostCommentChildPageResponse> getPostCommentChildPage(Long postCommentId, Long requestUserId, Pageable pageable) {
        List<GetPostCommentChildPageResponse> result;
        if(requestUserId == null) {
            result = postCommentChildRepository.getPostCommentChildPage(postCommentId, pageable);
        }
        else {
            result = postCommentChildRepository.getPostCommentChildPageWithMyLike(postCommentId, requestUserId, pageable);
        }
        Long count = postCommentChildRepository.getPostCommentChildPageCount(postCommentId);
        return new PageImpl(result, pageable, count);
    }

    @Override
    @Transactional
    public Long registerPostComment(Long postId, Long userId, PostCommentRegisterRequest postCommentRegisterRequest) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        PostComment postComment = postCommentService.registerComment(post, user, postCommentRegisterRequest.getContent());
        return postComment.getId();
    }

    @Override
    @Transactional
    public void modifyPostComment(Long postCommentId, Long userId, PostCommentModifyRequest postCommentModifyRequest) {
        //엔티티 로드 & 검증
        PostComment postComment = postCommentRepository.findById(postCommentId).orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        if(postComment.getUser().getId() != user.getId()) {
            throw new GeneralException(ErrorCode.FORBIDDEN);
        }
        // 수정
        if(postCommentModifyRequest.getContent() != null) {
            postComment.setContent(postCommentModifyRequest.getContent());
        }
    }

    @Override
    @Transactional
    public Long registerPostCommentChild(
            Long postCommentId,
            Long userId,
            PostCommentChildRegisterRequest postCommentChildRegisterRequest
    ) {
        PostComment postComment = postCommentRepository.findById(postCommentId).orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        PostCommentChild postCommentChild = postCommentService.registerCommentChild(postComment, user, postCommentChildRegisterRequest.getContent());
        return postCommentChild.getId();
    }

    @Override
    @Transactional
    public void modifyPostCommentChild(
            Long postCommentChildId,
            Long userId,
            PostCommentChildModifyRequest postCommentChildModifyRequest
    ) {
        //엔티티 로드 & 검증
        PostCommentChild postCommentChild = postCommentChildRepository.findById(postCommentChildId).orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        if(postCommentChild.getUser().getId() != user.getId()) {
            throw new GeneralException(ErrorCode.FORBIDDEN);
        }
        //수정
        if(postCommentChildModifyRequest.getContent() != null) {
            postCommentChild.setContent(postCommentChildModifyRequest.getContent());
        }
    }
}
