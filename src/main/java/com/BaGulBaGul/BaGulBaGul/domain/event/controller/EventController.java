package com.BaGulBaGul.BaGulBaGul.domain.event.controller;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventRegisterResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.GetLikeEventRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.GetLikeEventResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.IsMyLikeResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.LikeCountResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventController {
    ApiResponse<EventDetailResponse> getEventById(Long eventId);
    ApiResponse<Page<EventSimpleResponse>> getEventPageByCondition(EventConditionalRequest eventConditionalRequest, Pageable pageable);
    ApiResponse<EventRegisterResponse> registerEvent(Long userId, EventRegisterRequest eventRegisterRequest);
    ApiResponse<Object> modifyEvent(Long eventId, Long userId, EventModifyRequest eventModifyRequest);

    ApiResponse<Object> deleteEvent(Long eventId, Long userId);

    ApiResponse<LikeCountResponse> addLike(Long eventId, Long userId);
    ApiResponse<LikeCountResponse> deleteLike(Long eventId, Long userId);
    ApiResponse<IsMyLikeResponse> isMyLike(Long eventId, Long userId);
    ApiResponse<Page<GetLikeEventResponse>> getMyLike(Long userId, GetLikeEventRequest getLikeEventRequest, Pageable pageable);
}
