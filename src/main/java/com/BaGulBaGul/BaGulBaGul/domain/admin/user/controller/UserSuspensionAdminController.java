package com.BaGulBaGul.BaGulBaGul.domain.admin.user.controller;

import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.LiftUserSuspensionApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.SuspendUserApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.LiftUserSuspensionRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.SuspendUserRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserSuspensionService;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.validation.ValidationUtil;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin/user/suspension")
@RequiredArgsConstructor
@Api(tags = "사용자 정지 관리")
@PreAuthorize("hasAuthority('MANAGE_USER')")
public class UserSuspensionAdminController {

    private final UserSuspensionService userSuspensionService;

    @PostMapping("/{userId}")
    @Operation(summary = "사용자 정지", description = "사용자를 정지합니다. "
            + "이미 정지된 사용자일 경우 기한을 수정합니다. "
            + "정지 종료시각은 현재보다 이전일 수 없습니다.")
    public ApiResponse<Void> suspendUser(
            @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo,
            @PathVariable Long userId,
            @RequestBody @Valid SuspendUserApiRequest request
    ) {
        SuspendUserRequest serviceRequest = new SuspendUserRequest(request.getReason(), request.getEndDate());
        ValidationUtil.validate(serviceRequest);

        userSuspensionService.suspendUser(
                authenticatedUserInfo.getUserId(),
                userId,
                serviceRequest
        );
        return ApiResponse.of(null);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "사용자 정지 해제", description = "사용자의 정지를 해제합니다.")
    public ApiResponse<Void> liftSuspension(
            @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo,
            @PathVariable Long userId,
            @RequestBody @Valid LiftUserSuspensionApiRequest request
    ) {
        LiftUserSuspensionRequest serviceRequest = new LiftUserSuspensionRequest(request.getReason());
        ValidationUtil.validate(serviceRequest);

        userSuspensionService.liftSuspension(
                authenticatedUserInfo.getUserId(),
                userId,
                serviceRequest
        );
        return ApiResponse.of(null);
    }
}