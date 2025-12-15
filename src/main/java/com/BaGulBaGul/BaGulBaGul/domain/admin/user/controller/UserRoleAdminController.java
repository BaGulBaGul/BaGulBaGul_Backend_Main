package com.BaGulBaGul.BaGulBaGul.domain.admin.user.controller;

import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.AddUserRoleApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.RemoveUserRoleApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.SetUserRoleApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserRoleService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/user/{userId}/roles")
@RequiredArgsConstructor
@Api(tags = "관리자 - 사용자 관리 - 사용자 역할 관리")
@PreAuthorize("hasAuthority('MANAGE_USER')")
public class UserRoleAdminController {
    private final UserRoleService userRoleService;

    @PutMapping("/")
    @Operation(summary = "대상 유저의 역할을 설정", description = "put을 이용해 한번에 대체")
    public ApiResponse<Void> setUserRole(
            @PathVariable Long userId,
            @RequestBody SetUserRoleApiRequest setUserRoleApiRequest
    ) {
        userRoleService.changeRole(userId, setUserRoleApiRequest.getRoles());
        return ApiResponse.empty();
    }

    @PostMapping("/")
    @Operation(summary = "대상 유저의 역할을 추가")
    public ApiResponse<Void> addUserRole(
            @PathVariable Long userId,
            @RequestBody AddUserRoleApiRequest addUserRoleApiRequest
    ) {
        userRoleService.addRoles(userId, addUserRoleApiRequest.getRoles());
        return ApiResponse.empty();
    }

    @DeleteMapping("/")
    @Operation(summary = "대상 유저의 역할을 제거")
    public ApiResponse<Void> removeUserRole(
            @PathVariable Long userId,
            @RequestBody RemoveUserRoleApiRequest removeUserRoleApiRequest
    ) {
        userRoleService.addRoles(userId, removeUserRoleApiRequest.getRoles());
        return ApiResponse.empty();
    }
}
