package com.BaGulBaGul.BaGulBaGul.domain.event.controller;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.request.EventModifyApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.request.EventPageApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.request.EventRegisterApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.request.GetLikeEventApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.response.EventDetailApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.response.EventIdApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.response.EventPageApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.response.GetLikeEventApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.IsMyLikeResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.LikeCountResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventController {
    ApiResponse<EventDetailApiResponse> getEventById(Long eventId);
    ApiResponse<Page<EventPageApiResponse>> getEventPageByCondition(EventPageApiRequest eventPageApiRequest, Pageable pageable);
    ApiResponse<EventIdApiResponse> registerEvent(Long userId, EventRegisterApiRequest eventRegisterApiRequest);
    ApiResponse<Object> modifyEvent(Long eventId, Long userId, EventModifyApiRequest eventModifyApiRequest);

    ApiResponse<Object> deleteEvent(Long eventId, Long userId);

    ApiResponse<LikeCountResponse> addLike(Long eventId, Long userId);
    ApiResponse<LikeCountResponse> deleteLike(Long eventId, Long userId);
    ApiResponse<IsMyLikeResponse> isMyLike(Long eventId, Long userId);
    ApiResponse<Page<GetLikeEventApiResponse>> getMyLike(Long userId, GetLikeEventApiRequest getLikeEventApiRequest, Pageable pageable);
}
