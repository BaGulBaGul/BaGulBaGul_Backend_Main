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
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventController {
    ApiResponse<EventDetailApiResponse> getEventById(Long eventId);
    ApiResponse<Page<EventPageApiResponse>> getEventPageByCondition(
            EventPageApiRequest eventPageApiRequest,
            Pageable pageable
    );
    ApiResponse<EventIdApiResponse> registerEvent(
            AuthenticatedUserInfo authenticatedUserInfo,
            EventRegisterApiRequest eventRegisterApiRequest
    );
    ApiResponse<Object> modifyEvent(
            Long eventId,
            AuthenticatedUserInfo authenticatedUserInfo,
            EventModifyApiRequest eventModifyApiRequest
    );

    ApiResponse<Object> deleteEvent(
            Long eventId,
            AuthenticatedUserInfo authenticatedUserInfo
    );

    ApiResponse<LikeCountResponse> addLike(
            Long eventId,
            AuthenticatedUserInfo authenticatedUserInfo
    );
    ApiResponse<LikeCountResponse> deleteLike(
            Long eventId,
            AuthenticatedUserInfo authenticatedUserInfo
    );
    ApiResponse<IsMyLikeResponse> isMyLike(
            Long eventId,
            AuthenticatedUserInfo authenticatedUserInfo
    );
    ApiResponse<Page<GetLikeEventApiResponse>> getMyLike(
            AuthenticatedUserInfo authenticatedUserInfo,
            GetLikeEventApiRequest getLikeEventApiRequest,
            Pageable pageable
    );
}
