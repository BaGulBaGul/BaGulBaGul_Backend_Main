package com.BaGulBaGul.BaGulBaGul.domain.event.controller;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventRegisterResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.GetLikeEventRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.GetLikeEventResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.service.EventService;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.IsMyLikeResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class EventControllerImpl implements EventController {

    private final EventService eventService;

    @Override
    @GetMapping("/{eventId}")
    public ApiResponse<EventDetailResponse> getEventById(
            @PathVariable(name="eventId") Long eventId
    ) {
        EventDetailResponse eventDetailResponse = eventService.getEventDetailById(eventId);
        return ApiResponse.of(eventDetailResponse);
    }

    @Override
    @GetMapping("")
    public ApiResponse<Page<EventSimpleResponse>> getEventPageByCondition(
            EventConditionalRequest eventConditionalRequest,
            Pageable pageable
    ) {
        return ApiResponse.of(
                eventService.getEventPageByCondition(eventConditionalRequest, pageable)
        );
    }

    @Override
    @PostMapping("")
    public ApiResponse<EventRegisterResponse> registerEvent(
            @AuthenticationPrincipal Long userId,
            @RequestBody EventRegisterRequest eventRegisterRequest
    ) {
        Long eventId = eventService.registerEvent(userId, eventRegisterRequest);
        return ApiResponse.of(
                new EventRegisterResponse(eventId)
        );
    }

    @Override
    @PatchMapping("/{eventId}")
    public ApiResponse<Object> modifyEvent(
            @PathVariable(name="eventId") Long eventId,
            @AuthenticationPrincipal Long userId,
            @RequestBody EventModifyRequest eventModifyRequest
    ) {
        eventService.modifyEvent(eventId, userId, eventModifyRequest);
        return ApiResponse.of(null);
    }

    @Override
    @DeleteMapping("/{eventId}")
    public ApiResponse<Object> deleteEvent(
            @PathVariable(name="eventId") Long eventId,
            @AuthenticationPrincipal Long userId
    ) {
        eventService.deleteEvent(eventId, userId);
        return ApiResponse.of(null);
    }

    @Override
    @PostMapping("/{eventId}/like")
    public ApiResponse<Object> addLike(
            @PathVariable(name="eventId") Long eventId,
            @AuthenticationPrincipal Long userId
    ) {
        try {
            eventService.addLike(eventId, userId);
        } catch (DuplicateLikeException duplicateLikeException) {
        }
        return ApiResponse.of(null);
    }

    @Override
    @DeleteMapping("/{eventId}/like")
    public ApiResponse<Object> deleteLike(
            @PathVariable(name="eventId") Long eventId,
            @AuthenticationPrincipal Long userId
    ) {
        try {
            eventService.deleteLike(eventId, userId);
        } catch (LikeNotExistException likeNotExistException) {
        }
        return ApiResponse.of(null);
    }

    @Override
    @GetMapping("/{eventId}/ismylike")
    public ApiResponse<IsMyLikeResponse> isMyLike(
            @PathVariable(name="eventId") Long eventId,
            @AuthenticationPrincipal Long userId
    ) {
        return ApiResponse.of(
                new IsMyLikeResponse(eventService.isMyLike(eventId, userId))
        );
    }

    @Override
    @GetMapping("/mylike")
    public ApiResponse<Page<GetLikeEventResponse>> getMyLike(
            @AuthenticationPrincipal Long userId,
            GetLikeEventRequest getLikeEventRequest,
            Pageable pageable
    ) {
        return ApiResponse.of(
                eventService.getMyLikeEvent(getLikeEventRequest, userId, pageable)
        );
    }
}
