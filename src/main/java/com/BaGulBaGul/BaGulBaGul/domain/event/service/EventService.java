package com.BaGulBaGul.BaGulBaGul.domain.event.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.Category;
import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventDetailInfo;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventSimpleInfo;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.GetLikeEventRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.GetLikeEventResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import com.BaGulBaGul.BaGulBaGul.global.exception.NoPermissionException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventService {

    EventSimpleInfo getEventSimpleInfoById(Long eventId);
    EventDetailInfo getEventDetailInfoById(Long eventId);

    //연결된 자원 등을 포함한 이벤트의 모든 정보인 EventDetailResponse를 반환
    EventDetailResponse getEventDetailById(Long eventId);

    //조건에 맞는 모든 이벤트에 대한 EventSimpleResponse를 Page로 반환
    Page<EventSimpleResponse> getEventPageByCondition(EventConditionalRequest eventConditionalRequest, Pageable pageable);
    //순서를 유지한 채로 eventIds의 모든 이벤트에 대한 EventSimpleResponse를 반환
    List<EventSimpleResponse> getEventSimpleResponseByIds(List<Long> eventIds);

    //유저가 좋아요를 누른 이벤트를 페이지로 반환
    Page<GetLikeEventResponse> getMyLikeEvent(GetLikeEventRequest getLikeEventRequest, Long userId, Pageable pageable);

    //이벤트 등록
    Long registerEvent(Long userId, EventRegisterRequest eventRegisterRequest);
    //이벤트 수정
    void modifyEvent(Long eventId, Long userId, EventModifyRequest eventModifyRequest);
    //이벤트 삭제
    void deleteEvent(Long eventId, Long userId);

    //어떤 이벤트의 좋아요 개수를 반환
    int getLikeCount(Long eventId);
    //이벤트에 좋아요를 추가
    void addLike(Long eventId, Long userId) throws DuplicateLikeException;
    //이벤트에 좋아요를 삭제
    void deleteLike(Long eventId, Long userId) throws LikeNotExistException;
    //이벤트에 좋아요를 눌렀는지 여부
    boolean isMyLike(Long eventId, Long userId);

    //카테고리 초기화
    void clearCategory(Event event);
    //카테고리 추가
    void addCategories(Event event, List<String> categoryNames);
    //카테고리 추가
    void addCategory(Event event, String categoryName);
    //카테고리 추가
    void addCategory(Event event, Category category);

    //유저에세 이벤트에 대한 쓰기 권한이 있는지 확인
    void checkWritePermission(Long userId, Event event) throws NoPermissionException;
}
