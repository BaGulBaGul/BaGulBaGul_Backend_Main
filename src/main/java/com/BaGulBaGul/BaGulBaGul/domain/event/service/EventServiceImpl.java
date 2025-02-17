package com.BaGulBaGul.BaGulBaGul.domain.event.service;

import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.LocationModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.LocationRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.ParticipantStatusModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.ParticipantStatusRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.PeriodModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.PeriodRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.Category;
import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.EventCategory;
import com.BaGulBaGul.BaGulBaGul.domain.event.applicationevent.NewEventLikeApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventDetailInfo;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventSimpleInfo;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.GetLikeEventRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.GetLikeEventResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.exception.CategoryNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.event.exception.EventNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventCategoryRepository;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.CategoryRepository;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventRepository;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.querydsl.FindEventByCondition.EventIdsWithTotalCountOfPageResult;
import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostDetailInfo;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostService;
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
public class EventServiceImpl implements EventService {
    
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final CategoryRepository categoryRepository;

    private final PostService postService;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public EventSimpleInfo getEventSimpleInfoById(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException());
        return EventSimpleInfo.builder()
                .eventId(event.getId())
                .type(event.getType())
                .abstractLocation(event.getAbstractLocation())
                .currentHeadCount(event.getCurrentHeadCount())
                .maxHeadCount(event.getMaxHeadCount())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .categories(
                        event.getCategories().stream()
                                .map(eventCategory -> eventCategory.getCategory().getName())
                                .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    public EventDetailInfo getEventDetailInfoById(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException());
        return EventDetailInfo.builder()
                .eventId(event.getId())
                .type(event.getType())
                .currentHeadCount(event.getCurrentHeadCount())
                .maxHeadCount(event.getMaxHeadCount())
                .fullLocation(event.getFullLocation())
                .abstractLocation(event.getAbstractLocation())
                .latitudeLocation(event.getLatitudeLocation())
                .longitudeLocation(event.getLongitudeLocation())
                .ageLimit(event.getAgeLimit())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .categories(
                        event.getCategories().stream()
                                .map(eventCategory -> eventCategory.getCategory().getName())
                                .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    @Transactional
    public EventDetailResponse getEventDetailById(Long eventId) {
        Event event = eventRepository.findWithPostAndUserAndCategoryById(eventId).orElseThrow(() -> new EventNotFoundException());

        //삭제된 이벤트
        if(event.getDeleted()) {
            throw new EventNotFoundException();
        }

        //필요한 정보 추출
        EventDetailInfo eventDetailInfo = getEventDetailInfoById(eventId);
        PostDetailInfo postDetailInfo = postService.getPostDetailInfo(event.getPost().getId());

        //응답 dto 생성
        EventDetailResponse eventDetailResponse = EventDetailResponse.builder()
                .event(eventDetailInfo)
                .post(postDetailInfo)
                .build();

        return eventDetailResponse;
    }

    @Override
    @Transactional
    public Page<EventSimpleResponse> getEventPageByCondition(
            EventConditionalRequest eventConditionalRequest,
            Pageable pageable
    ) {
        //조건에 해당하는 event id를 페이지 검색함.
        EventIdsWithTotalCountOfPageResult pageResult = eventRepository.getEventIdsByConditionAndPageable(eventConditionalRequest, pageable);
        //필요한 정보를 fetch join
        eventRepository.findWithPostAndUserAndCategoriesByIds(pageResult.getEventIds());
        //페이지 조회한 ids를 순서대로 EventSimpleResponse로 변환
        List<EventSimpleResponse> eventSimpleResponses = pageResult.getEventIds()
                .stream()
                .map(eventRepository::findById)
                .map(event -> new EventSimpleResponse(
                                getEventSimpleInfoById(event.get().getId()),
                                postService.getPostSimpleInfo(event.get().getPost().getId())))
                .collect(Collectors.toList());
        //페이지 정보로 변환
        return new PageImpl<>(eventSimpleResponses, pageable, pageResult.getTotalCount());
    }

    @Override
    @Transactional
    public Page<GetLikeEventResponse> getMyLikeEvent(
            GetLikeEventRequest getLikeEventRequest,
            Long userId,
            Pageable pageable
    ) {
        Page<Event> events = eventRepository.getLikeEventByUserAndType(userId, getLikeEventRequest.getType(), pageable);
        //post와 fetch join
        if(events.getNumberOfElements() > 0) {
            List<Long> ids = events.stream().map(Event::getId).collect(Collectors.toList());
            eventRepository.findWithPostAndUserByIds(ids);
        }
        return events.map(GetLikeEventResponse::of);
    }

    @Override
    @Transactional
    public Long registerEvent(Long userId, EventRegisterRequest eventRegisterRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        //게시글 생성
        Post post = postService.registerPost(user, eventRegisterRequest.getPostRegisterRequest());
        //이벤트 생성
        LocationRegisterRequest locationRegisterRequest = eventRegisterRequest.getLocationRegisterRequest();
        PeriodRegisterRequest periodRegisterRequest = eventRegisterRequest.getPeriodRegisterRequest();
        ParticipantStatusRegisterRequest participantStatusRegisterRequest = eventRegisterRequest
                .getParticipantStatusRegisterRequest();
        Event event = Event.builder()
                .type(eventRegisterRequest.getType())
                .post(post)
                .ageLimit(eventRegisterRequest.getAgeLimit())
                .currentHeadCount(participantStatusRegisterRequest.getCurrentHeadCount())
                .maxHeadCount(participantStatusRegisterRequest.getMaxHeadCount())
                .fullLocation(locationRegisterRequest.getFullLocation())
                .abstractLocation(locationRegisterRequest.getAbstractLocation())
                .latitudeLocation(locationRegisterRequest.getLatitudeLocation())
                .longitudeLocation(locationRegisterRequest.getLongitudeLocation())
                .startDate(periodRegisterRequest.getStartDate())
                .endDate(periodRegisterRequest.getEndDate())
                .build();
        //카테고리 추가
        addCategories(event, eventRegisterRequest.getCategories());
        //저장
        eventRepository.save(event);
        return event.getId();
    }

    @Override
    @Transactional
    public void modifyEvent(Long eventId, Long userId, EventModifyRequest eventModifyRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException());

        //삭제된 이벤트
        if(event.getDeleted()) {
            throw new EventNotFoundException();
        }

        //요청한 유저가 작성자가 아닐 경우 수정 권한 없음
        checkWritePermission(userId, event);

        //patch 방식으로 eventModifyRequest에서 null이 아닌 모든 필드를 변경
        //post관련은 postService에 위임
        postService.modifyPost(event.getPost(), eventModifyRequest.getPostModifyRequest());

        //나머지 event관련 속성 변경
        if(eventModifyRequest.getType() != null) {
            event.setType(eventModifyRequest.getType());
        }
        if(eventModifyRequest.getAgeLimit() != null) {
            event.setAgeLimit(eventModifyRequest.getAgeLimit());
        }
        if(eventModifyRequest.getCategories() != null) {
            clearCategory(event);
            addCategories(event, eventModifyRequest.getCategories());
        }
        //참가자
        ParticipantStatusModifyRequest participantStatusModifyRequest = eventModifyRequest
                .getParticipantStatusModifyRequest();
        if(participantStatusModifyRequest.getCurrentHeadCount().isPresent()) {
            event.setCurrentHeadCount(participantStatusModifyRequest.getCurrentHeadCount().get());
        }
        if(participantStatusModifyRequest.getMaxHeadCount().isPresent()) {
            event.setMaxHeadCount(participantStatusModifyRequest.getMaxHeadCount().get());
        }
        //위치
        LocationModifyRequest locationModifyRequest = eventModifyRequest.getLocationModifyRequest();
        if(locationModifyRequest.getFullLocation().isPresent()) {
            event.setFullLocation(locationModifyRequest.getFullLocation().get());
        }
        if(locationModifyRequest.getAbstractLocation().isPresent()) {
            event.setAbstractLocation(locationModifyRequest.getAbstractLocation().get());
        }
        if(locationModifyRequest.getLatitudeLocation().isPresent()) {
            event.setLatitudeLocation(locationModifyRequest.getLatitudeLocation().get());
        }
        if(locationModifyRequest.getLongitudeLocation().isPresent()) {
            event.setLongitudeLocation(locationModifyRequest.getLongitudeLocation().get());
        }
        //기간
        PeriodModifyRequest periodModifyRequest = eventModifyRequest.getPeriodModifyRequest();
        if(periodModifyRequest.getStartDate().isPresent()) {
            event.setStartDate(periodModifyRequest.getStartDate().get());
        }
        if(periodModifyRequest.getEndDate().isPresent()) {
            event.setEndDate(periodModifyRequest.getEndDate().get());
        }
    }

    @Override
    @Transactional
    public void deleteEvent(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException());

        //삭제된 이벤트
        if(event.getDeleted()) {
            throw new EventNotFoundException();
        }

        //요청한 유저가 작성자가 아닐 경우 수정 권한 없음
        checkWritePermission(userId, event);

        event.setDeleted(true);
    }

    @Override
    @Transactional
    public int getLikeCount(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException());
        return event.getPost().getLikeCount();
    }

    @Override
    @Transactional
    public void addLike(Long eventId, Long userId) throws DuplicateLikeException {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException());
        //삭제된 이벤트
        if(event.getDeleted()) {
            throw new EventNotFoundException();
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        postService.addLike(event.getPost(), user);
        applicationEventPublisher.publishEvent(new NewEventLikeApplicationEvent(eventId, userId));
    }

    @Override
    @Transactional
    public void deleteLike(Long eventId, Long userId) throws LikeNotExistException {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException());
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        postService.deleteLike(event.getPost(), user);
    }

    @Override
    @Transactional
    public boolean isMyLike(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException());
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        return postService.existsLike(event.getPost(), user);
    }

    @Override
    @Transactional
    public void clearCategory(Event event) {
        //event.getCategories()가 empty인지 확인
        //eventCategoryRepository.deleteAllByEvent(event);
        eventCategoryRepository.deleteAll(event.getCategories());
        event.getCategories().clear();
        eventCategoryRepository.flush();
    }

    @Override
    @Transactional
    public void addCategories(Event event, List<String> categoryNames) {
        for(String categoryName: categoryNames) {
            addCategory(event, categoryName);
        }
    }

    @Override
    @Transactional
    public void addCategory(Event event, String categoryName) {
        Category category = categoryRepository.findByName(categoryName).orElseThrow(CategoryNotFoundException::new);
        addCategory(event, category);
    }

    @Override
    @Transactional
    public void addCategory(Event event, Category category) {
        EventCategory eventCategory = new EventCategory(event, category);
        event.getCategories().add(eventCategory);
    }

    @Override
    //어떤 유저의 어떤 이벤트에 대한 쓰기 권한을 확인
    public void checkWritePermission(Long userId, Event event) throws NoPermissionException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        //게시글에 대한 권한 확인에 위임
        postService.checkWritePermission(event.getPost(), user);
    }
}
