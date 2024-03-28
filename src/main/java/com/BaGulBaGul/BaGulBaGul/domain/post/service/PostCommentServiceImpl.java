package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostComment;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentChild;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentChildLike;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentLike;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.*;
import com.BaGulBaGul.BaGulBaGul.domain.post.event.NewPostCommentChildEvent;
import com.BaGulBaGul.BaGulBaGul.domain.post.event.NewPostCommentChildLikeEvent;
import com.BaGulBaGul.BaGulBaGul.domain.post.event.NewPostCommentEvent;
import com.BaGulBaGul.BaGulBaGul.domain.post.event.NewPostCommentLikeEvent;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.PostCommentChildNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.PostCommentNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.PostNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentChildLikeRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentChildRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentLikeRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.exception.UserNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.global.exception.NoPermissionException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostCommentServiceImpl implements PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostCommentChildRepository postCommentChildRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostCommentLikeRepository postCommentLikeRepository;
    private final PostCommentChildLikeRepository postCommentChildLikeRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public PostCommentDetailResponse getPostCommentDetail(Long postCommentId) {
        return postCommentRepository.getPostCommentDetail(postCommentId);
    }

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
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException());
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        //먼저 w lock획득 후 s lock(FK)을 얻어야 deadlock 방지
        //댓글 수 증가
        postRepository.increaseCommentCount(post);
        PostComment postComment = PostComment.builder()
                .post(post)
                .user(user)
                .content(postCommentRegisterRequest.getContent())
                .build();
        //댓글 등록
        postCommentRepository.save(postComment);
        //댓글 등록 이벤트 발행
        applicationEventPublisher.publishEvent(
                new NewPostCommentEvent(postComment.getId())
        );
        return postComment.getId();
    }

    @Override
    @Transactional
    public void modifyPostComment(Long postCommentId, Long userId, PostCommentModifyRequest postCommentModifyRequest) {
        //엔티티 로드 & 검증
        PostComment postComment = postCommentRepository.findById(postCommentId).orElseThrow(() -> new PostCommentNotFoundException());
        //수정할 권한이 있는지
        if(!userId.equals(postComment.getUser().getId())) {
            throw new NoPermissionException();
        }
        // 수정
        if(postCommentModifyRequest.getContent() != null) {
            postComment.setContent(postCommentModifyRequest.getContent());
        }
    }

    @Override
    @Transactional
    public void deletePostComment(Long postCommentId, Long userId) {
        //엔티티 로드 & 검증
        PostComment postComment = postCommentRepository.findById(postCommentId).orElseThrow(() -> new PostCommentNotFoundException());
        //삭제할 권한이 있는지
        if(!userId.equals(postComment.getUser().getId())) {
            throw new NoPermissionException();
        }
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
    public Long registerPostCommentChild(
            Long postCommentId,
            Long userId,
            PostCommentChildRegisterRequest postCommentChildRegisterRequest
    ) {
        PostComment postComment = postCommentRepository.findById(postCommentId).orElseThrow(() -> new PostCommentNotFoundException());
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());

        //답글이라면 originalPostCommentChildId와 originalPostCommentChildWriter가 null이 아님.
        Long originalPostCommentChildId = null;
        User originalPostCommentChildWriter = null;
        if(postCommentChildRegisterRequest.getOriginalPostCommentChildId() != null) {
            //답장을 받을 대댓글 검색
            PostCommentChild originalPostCommentChild = postCommentChildRepository
                    .findById(postCommentChildRegisterRequest.getOriginalPostCommentChildId())
                    .orElse(null);
            //답장을 받을 대댓글이 같은 댓글 안에 존재한다면 답글로 인정
            if(
                    originalPostCommentChild != null &&
                    postComment.getId() == originalPostCommentChild.getPostComment().getId()
            ) {
                originalPostCommentChildId = originalPostCommentChild.getId();
                originalPostCommentChildWriter = originalPostCommentChild.getUser();
            }
        }

        //대댓글 등록
        postCommentRepository.increaseCommentChildCount(postComment);
        PostCommentChild postCommentChild = PostCommentChild.builder()
                .postComment(postComment)
                .user(user)
                .replyTargetUser(originalPostCommentChildWriter)
                .content(postCommentChildRegisterRequest.getContent())
                .build();
        postCommentChildRepository.save(postCommentChild);

        //대댓글 등록 이벤트 발행
        applicationEventPublisher.publishEvent(
                new NewPostCommentChildEvent(
                        postCommentChild.getId(),
                        originalPostCommentChildId
                )
        );
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
        PostCommentChild postCommentChild = postCommentChildRepository.findById(postCommentChildId).orElseThrow(() -> new PostCommentChildNotFoundException());
        //수정할 권한이 있는지
        if(!userId.equals(postCommentChild.getUser().getId())) {
            throw new NoPermissionException();
        }
        //수정
        if(postCommentChildModifyRequest.getContent() != null) {
            postCommentChild.setContent(postCommentChildModifyRequest.getContent());
        }
    }

    @Override
    @Transactional
    public void deletePostCommentChild(Long postCommentChildId, Long userId) {
        //엔티티 로드 & 검증
        PostCommentChild postCommentChild = postCommentChildRepository.findById(postCommentChildId).orElseThrow(() -> new PostCommentChildNotFoundException());
        //삭제할 권한이 있는지
        if(!userId.equals(postCommentChild.getUser().getId())) {
            throw new NoPermissionException();
        }
        //대댓글과 연결된 좋아요 삭제
        postCommentChildLikeRepository.deleteAllByPostCommentChild(postCommentChild);
        //대댓글 삭제
        postCommentChildRepository.delete(postCommentChild);
        //댓글의 대댓글 개수 1 감소
        postCommentRepository.decreaseCommentChildCount(postCommentChild.getPostComment());
    }

    @Override
    @Transactional(rollbackFor = DuplicateLikeException.class)
    public void addLikeToComment(Long postCommentId, Long userId) throws DuplicateLikeException {
        //엔티티 로드 & 검증
        PostComment postComment = postCommentRepository.findById(postCommentId).orElseThrow(() -> new PostCommentNotFoundException());
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        //이미 존재하는지 검색
        if(postCommentLikeRepository.existsByPostCommentAndUser(postComment, user)) {
            throw new DuplicateLikeException();
        }
        //먼저 w lock 획득 후 s lock(FK) 획득
        postCommentRepository.increaseLikeCount(postComment);
        try {
            postCommentLikeRepository.save(new PostCommentLike(postComment, user));
            postCommentLikeRepository.flush();
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new DuplicateLikeException();
        }
        //댓글 좋아요 추가 이벤트 발행
        applicationEventPublisher.publishEvent(
                new NewPostCommentLikeEvent(postComment.getId(), user.getId())
        );
    }

    @Override
    @Transactional(rollbackFor = LikeNotExistException.class)
    public void deleteLikeToComment(Long postCommentId, Long userId) throws LikeNotExistException {
        //엔티티 로드 & 검증
        PostComment postComment = postCommentRepository.findById(postCommentId).orElseThrow(() -> new PostCommentNotFoundException());
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        //존재하지 않는지 검색
        if(postCommentLikeRepository.existsByPostCommentAndUser(postComment, user) != true) {
            throw new LikeNotExistException();
        }
        //좋아요 삭제
        postCommentRepository.decreaseLikeCount(postComment);
        int deletedCnt = postCommentLikeRepository.deleteAndGetCountByPostCommentAndUser(postComment, user);
        //삭제한 행이 없다면 좋아요가 없었음
        if(deletedCnt == 0) {
            throw new LikeNotExistException();
        }
    }

    @Override
    @Transactional
    public boolean existsCommentLike(Long postCommentId, Long userId) {
        PostComment postComment = postCommentRepository.findById(postCommentId).orElseThrow(() -> new PostCommentNotFoundException());
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        return postCommentLikeRepository.existsByPostCommentAndUser(postComment, user);
    }

    @Override
    @Transactional
    public void addLikeToCommentChild(Long postCommentChildId, Long userId) throws DuplicateLikeException {
        PostCommentChild postCommentChild = postCommentChildRepository.findById(postCommentChildId)
                .orElseThrow(() -> new PostCommentChildNotFoundException());
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        //좋아요가 이미 존재하는지 검색
        if(postCommentChildLikeRepository.existsByPostCommentChildAndUser(postCommentChild, user)) {
            throw new DuplicateLikeException();
        }
        //좋아요 추가
        postCommentChildRepository.increaseLikeCount(postCommentChild);
        try {
            postCommentChildLikeRepository.save(new PostCommentChildLike(postCommentChild, user));
            postCommentChildLikeRepository.flush();
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new DuplicateLikeException();
        }
        //대댓글 좋아요 추가 이벤트 발행
        applicationEventPublisher.publishEvent(
                new NewPostCommentChildLikeEvent(postCommentChild.getId(), user.getId())
        );
    }

    @Override
    @Transactional
    public void deleteLikeToCommentChild(Long postCommentChildId, Long userId) throws LikeNotExistException {
        PostCommentChild postCommentChild = postCommentChildRepository.findById(postCommentChildId)
                .orElseThrow(() -> new PostCommentChildNotFoundException());
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        //존재하지 않는지 검색
        if(postCommentChildLikeRepository.existsByPostCommentChildAndUser(postCommentChild, user) != true) {
            throw new LikeNotExistException();
        }
        //좋아요 삭제
        postCommentChildRepository.decreaseLikeCount(postCommentChild);
        int deletedCnt = postCommentChildLikeRepository.deleteAndGetCountByPostCommentChildAndUser(postCommentChild, user);
        //삭제한 행이 없다면 좋아요가 없었음
        if(deletedCnt == 0) {
            throw new LikeNotExistException();
        }
    }

    @Override
    @Transactional
    public boolean existsCommentChildLike(Long postCommentChildId, Long userId) {
        PostCommentChild postCommentChild = postCommentChildRepository.findById(postCommentChildId)
                .orElseThrow(() -> new PostCommentChildNotFoundException());
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        return postCommentChildLikeRepository.existsByPostCommentChildAndUser(postCommentChild, user);
    }
}
