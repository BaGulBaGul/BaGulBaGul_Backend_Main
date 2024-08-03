package com.BaGulBaGul.BaGulBaGul.domain.recruitment.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.exception.EventNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostDetailInfo;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostImageService;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostService;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent.NewRecruitmentLikeApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.GetLikeRecruitmentResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentDetailInfo;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentSimpleInfo;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.exception.RecruitmentNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.RecruitmentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.querydsl.FindRecruitmentByCondition.RecruitmentIdsWithTotalCountOfPageResult;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.exception.UserNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.global.exception.NoPermissionException;
import com.BaGulBaGul.BaGulBaGul.global.upload.service.ResourceService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecruitmentServiceImpl implements RecruitmentService {

    private final RecruitmentRepository recruitmentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    private final PostService postService;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public RecruitmentSimpleInfo getRecruitmentSimpleInfoById(Long recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow(() -> new RecruitmentNotFoundException());
        return RecruitmentSimpleInfo.builder()
                .recruitmentId(recruitment.getId())
                .state(recruitment.getState())
                .currentHeadCount(recruitment.getCurrentHeadCount())
                .maxHeadCount(recruitment.getMaxHeadCount())
                .startDate(recruitment.getStartDate())
                .endDate(recruitment.getEndDate())
                .build();
    }

    @Override
    @Transactional
    public RecruitmentDetailInfo getRecruitmentDetailInfoById(Long recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow(() -> new RecruitmentNotFoundException());
        return RecruitmentDetailInfo.builder()
                .recruitmentId(recruitmentId)
                .eventId(recruitment.getEvent().getId())
                .state(recruitment.getState())
                .currentHeadCount(recruitment.getCurrentHeadCount())
                .maxHeadCount(recruitment.getMaxHeadCount())
                .startDate(recruitment.getStartDate())
                .endDate(recruitment.getEndDate())
                .build();
    }

    @Override
    @Transactional
    public RecruitmentDetailResponse getRecruitmentDetailById(Long recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findWithPostAndUserById(recruitmentId).orElseThrow(() -> new RecruitmentNotFoundException());

        //필요한 정보 추출
        RecruitmentDetailInfo recruitmentDetailInfo = getRecruitmentDetailInfoById(recruitmentId);
        PostDetailInfo postDetailInfo = postService.getPostDetailInfo(recruitment.getPost().getId());

        //응답 dto 생성
        RecruitmentDetailResponse response = RecruitmentDetailResponse.builder()
                .recruitment(recruitmentDetailInfo)
                .post(postDetailInfo)
                .build();

        //조회수 증가
        postRepository.increaseViewsById(recruitment.getPost().getId());

        //방금 조회한 조회수를 반영해줌.
        postDetailInfo.setViews(postDetailInfo.getViews() + 1);

        return response;
    }

    @Override
    @Transactional
    public Page<RecruitmentSimpleResponse> getRecruitmentPageByCondition(
            RecruitmentConditionalRequest recruitmentConditionalRequest, Pageable pageable
    ) {
        //조건을 이용해 페이지조회를 하고 결과를 받아온다
        RecruitmentIdsWithTotalCountOfPageResult pageResult = recruitmentRepository.getRecruitmentIdsByConditionAndPageable(
                recruitmentConditionalRequest,
                pageable
        );

        //fetch join 수행
        recruitmentRepository.findWithPostAndUserByIds(pageResult.getRecruitmentIds());

        //응답 dto에 정보를 담는다
        List<RecruitmentSimpleResponse> content = pageResult.getRecruitmentIds()
                .stream()
                .map(id -> recruitmentRepository.findById(id))
                .map(recruitment -> RecruitmentSimpleResponse.builder()
                        .recruitment(getRecruitmentSimpleInfoById(recruitment.get().getId()))
                        .post(postService.getPostSimpleInfo(recruitment.get().getPost().getId()))
                        .build()
                ).collect(Collectors.toList());

        //최종 페이지 객체를 만들고 반환
        return new PageImpl<>(content, pageable, pageResult.getTotalCount());
    }

    @Override
    @Transactional
    public Page<GetLikeRecruitmentResponse> getMyLikeRecruitment(Long userId, Pageable pageable) {
        Page<Recruitment> recruitments = recruitmentRepository.getLikeRecruitmentByUser(userId, pageable);
        //post와 fetch join
        if(recruitments.getNumberOfElements() > 0) {
            List<Long> ids = recruitments.stream().map(Recruitment::getId).collect(Collectors.toList());
            recruitmentRepository.findWithPostAndEventAndEventPostByIds(ids);
        }
        return recruitments.map(GetLikeRecruitmentResponse::of);
    }

    @Override
    @Transactional
    public Long registerRecruitment(Long eventId, Long userId, RecruitmentRegisterRequest recruitmentRegisterRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException());
        //게시글 생성
        Post post = postService.registerPost(user, recruitmentRegisterRequest.toPostRegisterRequest());
        Recruitment recruitment = Recruitment.builder()
                .event(event)
                .post(post)
                .currentHeadCount(0)
                .maxHeadCount(recruitmentRegisterRequest.getMaxHeadCount())
                .startDate(recruitmentRegisterRequest.getStartDate())
                .endDate(recruitmentRegisterRequest.getEndDate())
                .build();
        //저장
        recruitmentRepository.save(recruitment);
        return recruitment.getId();
    }

    @Override
    @Transactional
    public void modifyRecruitment(Long recruitmentId, Long userId, RecruitmentModifyRequest recruitmentModifyRequest) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow(() -> new RecruitmentNotFoundException());
        //요청한 유저가 작성자가 아닐 경우 수정 권한 없음
        if (!userId.equals(recruitment.getPost().getUser().getId())) {
            throw new NoPermissionException();
        }
        //patch 방식으로 recruitmentModifyRequest에서 null이 아닌 모든 필드를 변경
        //jsonnullable의 경우 null값 가능. present를 확인하고 변경
        //post관련은 postService에 위임
        postService.modifyPost(recruitment.getPost(), recruitmentModifyRequest.toPostModifyRequest());
        //나머지 recruitment관련 속성 변경
        if(recruitmentModifyRequest.getState() != null) {
            recruitment.setState(recruitmentModifyRequest.getState());
        }
        if(recruitmentModifyRequest.getCurrentHeadCount().isPresent()) {
            recruitment.setCurrentHeadCount(recruitmentModifyRequest.getCurrentHeadCount().get());
        }
        if(recruitmentModifyRequest.getMaxHeadCount().isPresent()) {
            recruitment.setMaxHeadCount(recruitmentModifyRequest.getMaxHeadCount().get());
        }
        if(recruitmentModifyRequest.getStartDate() != null) {
            recruitment.setStartDate(recruitmentModifyRequest.getStartDate());
        }
        if(recruitmentModifyRequest.getEndDate() != null) {
            recruitment.setEndDate(recruitmentModifyRequest.getEndDate());
        }
    }

    @Override
    @Transactional
    public void deleteRecruitment(Long recruitmentId, Long userId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow(() -> new RecruitmentNotFoundException());
        if (!userId.equals(recruitment.getPost().getUser().getId())) {
            throw new NoPermissionException();
        }
        postService.deletePost(recruitment.getPost());
        recruitmentRepository.delete(recruitment);
    }

    @Override
    @Transactional
    public int getLikeCount(Long recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow(() -> new RecruitmentNotFoundException());
        return recruitment.getPost().getLikeCount();
    }

    @Override
    @Transactional(rollbackFor = {DuplicateLikeException.class})
    public void addLike(Long recruitmentId, Long userId) throws DuplicateLikeException {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow(() -> new RecruitmentNotFoundException());
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        postService.addLike(recruitment.getPost(), user);
        //모집글 좋아요 추가 이벤트 발행
        applicationEventPublisher.publishEvent(new NewRecruitmentLikeApplicationEvent(recruitmentId, userId));
    }

    @Override
    @Transactional
    public void deleteLike(Long recruitmentId, Long userId) throws LikeNotExistException {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow(() -> new RecruitmentNotFoundException());
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        postService.deleteLike(recruitment.getPost(), user);
    }

    @Override
    @Transactional
    public boolean isMyLike(Long recruitmentId, Long userId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow(() -> new RecruitmentNotFoundException());
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        return postService.existsLike(recruitment.getPost(), user);
    }
}
