package com.BaGulBaGul.BaGulBaGul.domain.recruitment.service;

import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.ParticipantStatusModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.ParticipantStatusRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.PeriodModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.PeriodRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.exception.EventNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostDetailInfo;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostService;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent.NewRecruitmentLikeApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response.GetLikeRecruitmentResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request.RecruitmentConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response.RecruitmentDetailInfo;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response.RecruitmentDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request.RecruitmentModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request.RecruitmentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response.RecruitmentSimpleInfo;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response.RecruitmentSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.exception.RecruitmentNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.RecruitmentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.querydsl.FindRecruitmentByCondition.RecruitmentIdsWithTotalCountOfPageResult;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.exception.UserNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.global.exception.NoPermissionException;
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

        //삭제된 모집글은 제외
        if(recruitment.getDeleted()) {
            throw new RecruitmentNotFoundException();
        }

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
        //게시글 생성은 게시글 서비스에 위임
        Post post = postService.registerPost(user, recruitmentRegisterRequest.getPostRegisterRequest());
        //모집글 생성
        ParticipantStatusRegisterRequest participantStatusRegisterRequest = recruitmentRegisterRequest
                .getParticipantStatusRegisterRequest();
        PeriodRegisterRequest periodRegisterRequest = recruitmentRegisterRequest.getPeriodRegisterRequest();
        Recruitment recruitment = Recruitment.builder()
                .event(event)
                .post(post)
                .currentHeadCount(0)
                .maxHeadCount(participantStatusRegisterRequest.getMaxHeadCount())
                .startDate(periodRegisterRequest.getStartDate())
                .endDate(periodRegisterRequest.getEndDate())
                .build();
        //저장
        recruitmentRepository.save(recruitment);
        return recruitment.getId();
    }

    @Override
    @Transactional
    public void modifyRecruitment(Long recruitmentId, Long userId, RecruitmentModifyRequest recruitmentModifyRequest) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow(() -> new RecruitmentNotFoundException());
        //삭제된 모집글은 제외
        if(recruitment.getDeleted()) {
            throw new RecruitmentNotFoundException();
        }

        //요청한 유저의 쓰기 권한을 확인
        checkWritePermission(userId, recruitment);

        //patch 방식으로 recruitmentModifyRequest에서 null이 아닌 모든 필드를 변경
        //jsonnullable의 경우 null값 가능. isPresent를 확인하고 변경
        //post관련은 postService에 위임
        postService.modifyPost(recruitment.getPost(), recruitmentModifyRequest.getPostModifyRequest());
        //나머지 recruitment관련 속성 변경
        if(recruitmentModifyRequest.getState() != null) {
            recruitment.setState(recruitmentModifyRequest.getState());
        }
        //참여자
        ParticipantStatusModifyRequest participantStatusModifyRequest = recruitmentModifyRequest
                .getParticipantStatusModifyRequest();
        if(participantStatusModifyRequest.getCurrentHeadCount().isPresent()) {
            recruitment.setCurrentHeadCount(participantStatusModifyRequest.getCurrentHeadCount().get());
        }
        if(participantStatusModifyRequest.getMaxHeadCount().isPresent()) {
            recruitment.setMaxHeadCount(participantStatusModifyRequest.getMaxHeadCount().get());
        }
        //모집 기간
        PeriodModifyRequest periodModifyRequest = recruitmentModifyRequest.getPeriodModifyRequest();
        if(periodModifyRequest.getStartDate().isPresent()) {
            recruitment.setStartDate(periodModifyRequest.getStartDate().get());
        }
        if(periodModifyRequest.getEndDate().isPresent()) {
            recruitment.setEndDate(periodModifyRequest.getEndDate().get());
        }
    }

    @Override
    @Transactional
    public void deleteRecruitment(Long recruitmentId, Long userId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow(() -> new RecruitmentNotFoundException());
        //삭제된 모집글은 제외
        if(recruitment.getDeleted()) {
            throw new RecruitmentNotFoundException();
        }

        //요청한 유저의 쓰기 권한을 확인
        checkWritePermission(userId, recruitment);

        int updatedCount = recruitmentRepository.setDeletedTrueAndGetCountIfNotDeleted(recruitmentId);
        //중복요청 등의 이유로 이미 삭제된 경우
        if(updatedCount == 0) {
            throw new RecruitmentNotFoundException();
        }
    }

    @Override
    @Transactional
    public int getLikeCount(Long recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow(() -> new RecruitmentNotFoundException());
        return recruitment.getPost().getLikeCount();
    }

    @Override
    @Transactional
    public void addLike(Long recruitmentId, Long userId) throws DuplicateLikeException {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow(() -> new RecruitmentNotFoundException());
        //삭제된 모집글은 제외
        if(recruitment.getDeleted()) {
            throw new RecruitmentNotFoundException();
        }

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

    @Override
    //어떤 유저의 어떤 모집글에 대한 쓰기 권한을 확인
    public void checkWritePermission(Long userId, Recruitment recruitment) throws NoPermissionException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        //게시글에 대한 권한 확인에 위임
        postService.checkWritePermission(recruitment.getPost(), user);
    }
}
