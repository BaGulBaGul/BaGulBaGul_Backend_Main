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
    EventDetailResponse getEventDetailById(Long eventId);
    Page<EventSimpleResponse> getEventPageByCondition(EventConditionalRequest eventConditionalRequest, Pageable pageable);
    Page<GetLikeEventResponse> getMyLikeEvent(GetLikeEventRequest getLikeEventRequest, Long userId, Pageable pageable);
    Long registerEvent(Long userId, EventRegisterRequest eventRegisterRequest);
    void modifyEvent(Long eventId, Long userId, EventModifyRequest eventModifyRequest);
    void deleteEvent(Long eventId, Long userId);

    int getLikeCount(Long eventId);
    void addLike(Long eventId, Long userId) throws DuplicateLikeException;
    void deleteLike(Long eventId, Long userId) throws LikeNotExistException;
    boolean isMyLike(Long eventId, Long userId);

    void clearCategory(Event event);
    void addCategories(Event event, List<String> categoryNames);
    void addCategory(Event event, String categoryName);
    void addCategory(Event event, Category category);

    void checkWritePermission(Long userId, Event event) throws NoPermissionException;
}
