package com.BaGulBaGul.BaGulBaGul.domain.admin.user.controller;

import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.UserSearchByAdminApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.response.UserSearchByAdminApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.service.response.UserSearchByAdminResponse;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.service.UserAdminService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
@Api(tags = "관리자 - 사용자 관리")
@PreAuthorize("hasAuthority('MANAGE_USER')")
public class UserAdminController {
    private final UserAdminService userAdminService;

    @GetMapping("/")
    @Operation(summary = "조건에 맞는 유저를 페이징 조회",
            description = ""
    )
    public ApiResponse<Page<UserSearchByAdminApiResponse>> searchUser(
            UserSearchByAdminApiRequest userSearchByAdminApiRequest,
            Pageable pageable
    ) {
        Page<UserSearchByAdminResponse> searchResult = userAdminService.getUserPageByAdminSearch(
                userSearchByAdminApiRequest.toUserSearchRequest(),
                pageable
        );
        return ApiResponse.of(searchResult.map(UserSearchByAdminApiResponse::from));
    }
}
