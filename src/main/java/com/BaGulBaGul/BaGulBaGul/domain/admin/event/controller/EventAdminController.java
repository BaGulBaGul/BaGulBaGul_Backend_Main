package com.BaGulBaGul.BaGulBaGul.domain.admin.event.controller;

import com.BaGulBaGul.BaGulBaGul.domain.admin.event.dto.api.request.AdminEventPageApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.response.EventPageApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.service.EventService;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.validation.ValidationUtil;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/event")
@RequiredArgsConstructor
@Api(tags = "관리자 - 이벤트 관리", description = "MANAGE_EVENT 권한 필요")
@PreAuthorize("hasAuthority('MANAGE_EVENT')")
public class EventAdminController {

    private final EventService eventService;

    @GetMapping("/search")
    @Operation(summary = "조건에 맞는 이벤트를 페이징 조회 - 관리자 버전",
            description = "일반 이벤트 페이지 조회 api와 사용법이 같음. 삭제 여부를 조건에 포함"
    )
    public ApiResponse<Page<EventPageApiResponse>> getEventPageByCondition(
            AdminEventPageApiRequest adminEventPageApiRequest,
            Pageable pageable
    ) {
        //이벤트 조건검색 요청 변환 후 검증
        EventConditionalRequest eventConditionalRequest = adminEventPageApiRequest.toEventConditionalRequest();
        ValidationUtil.validate(eventConditionalRequest);
        //조건에 맞는 이벤트 페이지 검색
        Page<EventSimpleResponse> eventSimpleResponses = eventService.getEventPageByCondition(eventConditionalRequest,
                pageable);
        //api 응답 dto로 변환
        Page<EventPageApiResponse> eventPageApiResponse = eventSimpleResponses.map(EventPageApiResponse::from);
        return ApiResponse.of(eventPageApiResponse);
    }

    @PostMapping("/deleted/{eventId}/restore")
    @Operation(summary = "삭제된 이벤트를 복구",
            description = "삭제된 이벤트를 복구"
    )
    public ApiResponse<Void> restoreDeletedEvent(
            @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo,
            @PathVariable(name = "eventId") Long eventId
    ) {
        eventService.restoreEvent(authenticatedUserInfo, eventId);
        return ApiResponse.of(null);
    }
}
