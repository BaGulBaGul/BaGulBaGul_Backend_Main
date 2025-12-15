package com.BaGulBaGul.BaGulBaGul.domain.admin.user.controller;

import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.AddRolePermissionsApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.DeleteRolePermissionsApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.RoleRegisterApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.SearchRoleApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.response.SearchPermissionApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.response.SearchRoleApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.PermissionType;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.RoleRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.SearchRoleResponse;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.SearchRoleRequest;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.PermissionService;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.RoleService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.validation.ValidationUtil;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/user/role")
@RequiredArgsConstructor
@Api(tags = "관리자 - 사용자 관리 - 역할 관리")
public class RoleAdminController {

    private final RoleService roleService;
    private final PermissionService permissionService;

    @GetMapping("/")
    @Operation(summary = "역할을 검색", description = "name을 이용한 정렬 가능")
    @PreAuthorize("hasAnyAuthority('MANAGE_ROLE', 'READ_ROLE')")
    public ApiResponse<Page<SearchRoleApiResponse>> searchRole(
        SearchRoleApiRequest searchRoleApiRequest, Pageable pageable
    ) {
        SearchRoleRequest searchRoleRequest = searchRoleApiRequest.toSearchRoleRequest();
        ValidationUtil.validate(searchRoleRequest);
        Page<SearchRoleResponse> pageResult = roleService.findRoleByCondition(searchRoleRequest, pageable);
        return ApiResponse.of(pageResult.map(SearchRoleApiResponse::from));
    }

    @PostMapping("/")
    @Operation(summary = "역할을 추가")
    @PreAuthorize("hasAuthority('MANAGE_ROLE')")
    public ApiResponse<Void> addRole(
            @RequestBody RoleRegisterApiRequest roleRegisterApiRequest
    ) {
        RoleRegisterRequest roleRegisterRequest = roleRegisterApiRequest.toRoleRegisterRequest();
        ValidationUtil.validate(roleRegisterRequest);
        roleService.createRole(roleRegisterRequest);
        return ApiResponse.empty();
    }

    @DeleteMapping("/{roleName}")
    @Operation(summary = "역할을 제거")
    @PreAuthorize("hasAuthority('MANAGE_ROLE')")
    public ApiResponse<Void> deleteRole(
            @PathVariable("roleName") String roleName
    ) {
        roleService.deleteRole(roleName);
        return ApiResponse.empty();
    }

    @PostMapping("/{roleName}/permissions")
    @Operation(summary = "역할의 권한을 추가")
    @PreAuthorize("hasAuthority('MANAGE_ROLE')")
    public ApiResponse<Void> addPermissionsToRole(
            @PathVariable("roleName") String roleName,
            @RequestBody AddRolePermissionsApiRequest addRolePermissionsApiRequest
    ) {
        permissionService.addPermissions(roleName, addRolePermissionsApiRequest.getPermissions());
        return ApiResponse.empty();
    }

    @DeleteMapping("/{roleName}/permissions")
    @Operation(summary = "역할의 권한을 제거")
    @PreAuthorize("hasAuthority('MANAGE_ROLE')")
    public ApiResponse<Void> deletePermissionsFromRole(
            @PathVariable("roleName") String roleName,
            @RequestBody DeleteRolePermissionsApiRequest deleteRolePermissionsApiRequest
    ) {
        permissionService.deletePermissions(roleName, deleteRolePermissionsApiRequest.getPermissions());
        return ApiResponse.empty();
    }

    @GetMapping("/permissions")
    @Operation(summary = "모든 권한을 나열")
    @PreAuthorize("hasAuthority('READ_ROLE')")
    public ApiResponse<SearchPermissionApiResponse> searchPermission() {
        return ApiResponse.of(
                SearchPermissionApiResponse.builder()
                        .permissions(PermissionType.values())
                        .build()
        );
    }
}
