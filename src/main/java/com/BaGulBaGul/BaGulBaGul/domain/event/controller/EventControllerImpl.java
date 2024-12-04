package com.BaGulBaGul.BaGulBaGul.domain.event.controller;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.GetLikeEventRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.GetLikeEventResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.request.EventModifyApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.request.EventPageApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.request.EventRegisterApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.request.GetLikeEventApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.response.EventDetailApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.response.EventIdApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.response.EventPageApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.response.GetLikeEventApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.service.EventService;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.IsMyLikeResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.LikeCountResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
@Api(tags = "이벤트")
public class EventControllerImpl implements EventController {

    private final EventService eventService;

    @Override
    @GetMapping("/{eventId}")
    @Operation(summary = "이벤트 id를 받아 자세한 정보를 조회",
            description = "참고 : api 호출 시 조회수 1 증가"
    )
    public ApiResponse<EventDetailApiResponse> getEventById(
            @PathVariable(name="eventId") Long eventId
    ) {
        EventDetailResponse eventDetailResponse = eventService.getEventDetailById(eventId);
        return ApiResponse.of(EventDetailApiResponse.from(eventDetailResponse));
    }

    @Override
    @GetMapping("")
    @Operation(summary = "조건에 맞는 이벤트를 페이징 조회",
            description = "ex) http://localhost:8080/api/event?type=LOCAL_EVENT&title=테스트&categories=스포츠/레저&categories=문화/예술&tags=태그5&startDate=2023-11-13T00:00:00&endDate=2023-11-14T00:00:00&page=0&size=10&sort=startDate,desc\n"
                    + "모든 조건은 and로 처리됨.\n"
                    + "startDate, endDate는 해당 기간 내에 진행되는 모든 이벤트를 검색\n"
                    + "페이징 관련 파라메터는 \n "
                    + "http://localhost:8080/api/event?size=10&page=0&sort=startDate,asc&sort=views,desc\n "
                    + "이런 식으로 넘기면 됨. sort가 여러 개면 앞에서부터 순서대로 정렬 적용\n"
                    + "정렬 가능 속성 : createdAt, views, likeCount, commentCount, startDate, endDate, headCount\n"
                    + "파라메터로 명시하지 않거나 null인 조건은 무시되지만(즉 쿼리파라메터가 없으면 모든 이벤트 검색) 페이징은 기본 페이징 조건 적용됨(page 0 size 20)"
    )
    public ApiResponse<Page<EventPageApiResponse>> getEventPageByCondition(
            EventPageApiRequest eventPageApiRequest,
            Pageable pageable
    ) {
        EventConditionalRequest eventConditionalRequest = eventPageApiRequest.toEventConditionalRequest();
        Page<EventSimpleResponse> eventSimpleResponses = eventService.getEventPageByCondition(eventConditionalRequest,
                pageable);
        Page<EventPageApiResponse> eventPageApiResponse = eventSimpleResponses.map(EventPageApiResponse::from);
        return ApiResponse.of(eventPageApiResponse);
    }

    @Override
    @PostMapping("")
    @Operation(summary = "이벤트 등록 api",
            description = "로그인 필요\n"
                    + "생성한 이벤트 id 반환"
    )
    public ApiResponse<EventIdApiResponse> registerEvent(
            @AuthenticationPrincipal Long userId,
            @RequestBody EventRegisterApiRequest eventRegisterApiRequest
    ) {
        EventRegisterRequest eventRegisterRequest = eventRegisterApiRequest.toEventRegisterRequest();
        Long eventId = eventService.registerEvent(userId, eventRegisterRequest);
        return ApiResponse.of(new EventIdApiResponse(eventId));
    }

    @Override
    @PatchMapping("/{eventId}")
    @Operation(summary = "이벤트 수정 api",
            description = "로그인 필요\n"
                    + "PATCH방식. 변경하기 싫은 필드는 보내지 않거나 null로 보내면 됨"
    )
    public ApiResponse<Object> modifyEvent(
            @PathVariable(name="eventId") Long eventId,
            @AuthenticationPrincipal Long userId,
            @RequestBody EventModifyApiRequest eventModifyApiRequest
    ) {
        EventModifyRequest eventModifyRequest = eventModifyApiRequest.toEventModifyRequest();
        eventService.modifyEvent(eventId, userId, eventModifyRequest);
        return ApiResponse.of(null);
    }

    @Override
    @DeleteMapping("/{eventId}")
    @Operation(summary = "이벤트 삭제 api",
            description = "로그인 필요"
    )
    public ApiResponse<Object> deleteEvent(
            @PathVariable(name="eventId") Long eventId,
            @AuthenticationPrincipal Long userId
    ) {
        eventService.deleteEvent(eventId, userId);
        return ApiResponse.of(null);
    }

    @Override
    @PostMapping("/{eventId}/like")
    @Operation(summary = "이벤트 좋아요 등록 api",
            description = "로그인 필요\n"
                    + "유저당 한번만 좋아요 등록 가능\n"
                    + "이미 좋아요를 눌렀다면 무시됨"
    )
    public ApiResponse<LikeCountResponse> addLike(
            @PathVariable(name="eventId") Long eventId,
            @AuthenticationPrincipal Long userId
    ) {
        try {
            eventService.addLike(eventId, userId);
        } catch (DuplicateLikeException duplicateLikeException) {
        }
        return ApiResponse.of(
                new LikeCountResponse(
                        eventService.getLikeCount(eventId)
                )
        );
    }

    @Override
    @DeleteMapping("/{eventId}/like")
    @Operation(summary = "이벤트 좋아요 삭제 api",
            description = "로그인 필요\n"
                    + "삭제할 좋아요가 없다면 무시됨"
    )
    public ApiResponse<LikeCountResponse> deleteLike(
            @PathVariable(name="eventId") Long eventId,
            @AuthenticationPrincipal Long userId
    ) {
        try {
            eventService.deleteLike(eventId, userId);
        } catch (LikeNotExistException likeNotExistException) {
        }
        return ApiResponse.of(
                new LikeCountResponse(
                        eventService.getLikeCount(eventId)
                )
        );
    }

    @Override
    @GetMapping("/{eventId}/ismylike")
    @Operation(summary = "이벤트 좋아요 확인 api",
            description = "로그인 필요\n"
                    + "자신이 좋아요를 눌렀는지 boolean으로 확인"
    )
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
    @Operation(summary = "자신이 좋아요를 누른 모든 이벤트 검색",
            description = "로그인 필요\n"
                    + "이벤트 타입 파라메터 전달 필수\n"
                    + "페이징 지원"
    )
    public ApiResponse<Page<GetLikeEventApiResponse>> getMyLike(
            @AuthenticationPrincipal Long userId,
            GetLikeEventApiRequest getLikeEventApiRequest,
            Pageable pageable
    ) {
        GetLikeEventRequest getLikeEventRequest = getLikeEventApiRequest.toGetLikeEventRequest();
        Page<GetLikeEventResponse> myLikeEvents = eventService.getMyLikeEvent(getLikeEventRequest, userId, pageable);
        Page<GetLikeEventApiResponse> responses = myLikeEvents.map(GetLikeEventApiResponse::from);
        return ApiResponse.of(responses);
    }
}
