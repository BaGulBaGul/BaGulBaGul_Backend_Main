package com.BaGulBaGul.BaGulBaGul.domain.admin.event.controller;

import com.BaGulBaGul.BaGulBaGul.domain.admin.event.dto.api.request.EventBannerModifyApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventBannerModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.service.EventBannerService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.validation.ValidationUtil;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/event/banner")
@RequiredArgsConstructor
@Api(tags = "관리자 - 이벤트 배너 관리", description = "MANAGE_EVENT 권한 필요")
@PreAuthorize("hasAuthority('MANAGE_EVENT')")
public class EventBannerAdminController {

    private final EventBannerService eventBannerService;

    @PatchMapping("/")
    @Operation(summary = "이벤트 배너 설정 api",
            description = "설정을 원하는 배너만 리스트에 담아서 요청"
    )
    public ApiResponse<Void> modifyEventBanner(
            @RequestBody List<EventBannerModifyApiRequest> apiRequest
    ) {
        //서비스 dto로 변환
        List<EventBannerModifyRequest> eventBannerModifyRequest = apiRequest.stream()
                .map(EventBannerModifyApiRequest::toEventBannerModifyRequest)
                .collect(Collectors.toList());
        //검증
        eventBannerModifyRequest.stream().forEach(ValidationUtil::validate);
        //배너 설정 서비스 호출
        eventBannerService.setEventBanners(eventBannerModifyRequest);
        return ApiResponse.of(null);
    }
}
