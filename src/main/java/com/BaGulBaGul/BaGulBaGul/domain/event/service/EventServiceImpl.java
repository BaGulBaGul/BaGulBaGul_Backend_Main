package com.BaGulBaGul.BaGulBaGul.domain.event.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.Category;
import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.EventCategory;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.GetLikeEventRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.GetLikeEventResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.exception.CategoryNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.event.exception.EventNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventCategoryRepository;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.CategoryRepository;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostImage;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostImageService;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostService;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.exception.UserNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.global.exception.NoPermissionException;
import com.BaGulBaGul.BaGulBaGul.global.upload.repository.ResourceRepository;
import com.BaGulBaGul.BaGulBaGul.global.upload.service.ResourceService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final PostRepository postRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final ResourceRepository resourceRepository;

//    private final PostAPIService postService;
    private final PostService postService;
    private final PostImageService postImageService;
    private final ResourceService resourceService;

    @Override
    @Transactional
    public EventDetailResponse getEventDetailById(Long eventId) {
        Event event = eventRepository.findWithPostAndUserAndCategoryById(eventId).orElseThrow(() -> new EventNotFoundException());
        //연결된 이미지의 resource id와 url을 조회
        List<PostImage> images = postImageService.getByOrder(event.getPost());
        List<Long> resourceIds = images.stream().map(x -> x.getResource().getId()).collect(Collectors.toList());
        List<String> imageUrls = resourceService.getResourceUrlsFromIds(resourceIds);
        //조회수 증가
        postRepository.increaseViewsById(event.getPost().getId());
        //응답 dto 추출
        EventDetailResponse eventDetailResponse = EventDetailResponse.of(event, resourceIds, imageUrls);
        //방금 조회한 조회수를 반영해줌.
        eventDetailResponse.setViews(eventDetailResponse.getViews() + 1);
        return eventDetailResponse;
    }

    @Override
    public Page<EventSimpleResponse> getEventPageByCondition(
            EventConditionalRequest eventConditionalRequest,
            Pageable pageable
    ) {
        return eventRepository.getEventSimpleResponsePageByCondition(eventConditionalRequest, pageable);
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
            eventRepository.findWithPostByIds(ids);
        }
        return events.map(GetLikeEventResponse::of);
    }

    @Override
    @Transactional
    public Long registerEvent(Long userId, EventRegisterRequest eventRegisterRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        //게시글 생성
        Post post = postService.registerPost(user, eventRegisterRequest.toPostRegisterRequest());
        //이벤트 생성
        Event event = Event.builder()
                .type(eventRegisterRequest.getType())
                .post(post)
                .currentHeadCount(0)
                .totalHeadCount(eventRegisterRequest.getTotalHeadCount())
                .fullLocation(eventRegisterRequest.getFullLocation())
                .abstractLocation(eventRegisterRequest.getAbstractLocation())
                .latitudeLocation(eventRegisterRequest.getLatitudeLocation())
                .longitudeLocation(eventRegisterRequest.getLongitudeLocation())
                .startDate(eventRegisterRequest.getStartDate())
                .endDate(eventRegisterRequest.getEndDate()).build();
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
        //요청한 유저가 작성자가 아닐 경우 수정 권한 없음
        if(!userId.equals(event.getPost().getUser().getId())) {
            throw new NoPermissionException();
        }
        //patch 방식으로 eventModifyRequest에서 null이 아닌 모든 필드를 변경
        //post관련은 postService에 위임
        postService.modifyPost(event.getPost(), eventModifyRequest.toPostModifyRequest());
        //나머지 event관련 속성 변경
        if(eventModifyRequest.getType() != null) {
            event.setType(eventModifyRequest.getType());
        }
        if(eventModifyRequest.getCurrentHeadCount().isPresent()) {
            event.setCurrentHeadCount(eventModifyRequest.getCurrentHeadCount().get());
        }
        if(eventModifyRequest.getTotalHeadCount().isPresent()) {
            event.setTotalHeadCount(eventModifyRequest.getTotalHeadCount().get());
        }
        if(eventModifyRequest.getFullLocation() != null) {
            event.setFullLocation(eventModifyRequest.getFullLocation());
        }
        if(eventModifyRequest.getAbstractLocation() != null) {
            event.setAbstractLocation(eventModifyRequest.getAbstractLocation());
        }
        if(eventModifyRequest.getLatitudeLocation() != null) {
            event.setLatitudeLocation(eventModifyRequest.getLatitudeLocation());
        }
        if(eventModifyRequest.getLongitudeLocation() != null) {
            event.setLongitudeLocation(eventModifyRequest.getLongitudeLocation());
        }
        if(eventModifyRequest.getStartDate() != null) {
            event.setStartDate(eventModifyRequest.getStartDate());
        }
        if(eventModifyRequest.getEndDate() != null) {
            event.setEndDate(eventModifyRequest.getEndDate());
        }
        if(eventModifyRequest.getCategories() != null) {
            clearCategory(event);
            addCategories(event, eventModifyRequest.getCategories());
        }
    }

    @Override
    @Transactional
    public void deleteEvent(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException());
        //요청한 유저가 작성자가 아닐 경우 수정 권한 없음
        if(!userId.equals(event.getPost().getUser().getId())) {
            throw new NoPermissionException();
        }
        postService.deletePost(event.getPost());
        eventCategoryRepository.deleteAllByEvent(event);
        eventRepository.delete(event);
    }

    @Override
    @Transactional(rollbackFor = {DuplicateLikeException.class})
    public void addLike(Long eventId, Long userId) throws DuplicateLikeException {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException());
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        postService.addLike(event.getPost(), user);
    }

    @Override
    @Transactional(rollbackFor = {LikeNotExistException.class})
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
}
