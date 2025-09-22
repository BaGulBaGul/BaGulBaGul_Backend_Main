package com.BaGulBaGul.BaGulBaGul.domain.admin.user.controller;

import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.AMEHUserRegisterApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.AMEHUserSearchByAdminApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.AMEHUserUpdateApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.UserSearchByAdminApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.response.UserSearchByAdminApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.service.response.UserSearchByAdminResponse;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.service.UserAdminService;
import com.BaGulBaGul.BaGulBaGul.domain.user.AdminManageEventHostUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.AdminManageEventHostUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.AdminManageEventHostUserModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.querydsl.FindUserByCondition;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserInfoService;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/user/amehuser")
@RequiredArgsConstructor
@Api(tags = "사용자 관리 - 이벤트 호스트 유저 관리")
@PreAuthorize("hasAuthority('MANAGE_USER')")
public class EventHostUserAdminController {

    private final UserJoinService userJoinService;
    private final UserInfoService userInfoService;
    private final FindUserByCondition findUserByCondition;
    private final UserAdminService userAdminService;

    //조회
    @GetMapping("/")
    @Operation(summary = "이벤트 호스트 유저 페이지 검색")
    public ApiResponse<Page<UserSearchByAdminApiResponse>> searchAdminManageEventHostUser(
            AMEHUserSearchByAdminApiRequest apiRequest, Pageable pageable
    ) {
        Page<UserSearchByAdminResponse> pageResult = userAdminService.getUserPageByAdminSearch(
                apiRequest.toUserSearchRequest(), pageable);
        return ApiResponse.of(pageResult.map(UserSearchByAdminApiResponse::from));
    }

    //등록
    @PostMapping("/")
    @Operation(summary = "이벤트 호스트 유저 등록")
    public ApiResponse<Long> registerAdminManageEventHostUser(
            @RequestBody AMEHUserRegisterApiRequest apiRequest
    ) {
        AdminManageEventHostUserJoinRequest serviceRequest = apiRequest.toServiceRequest();
        ValidationUtil.validate(serviceRequest);
        AdminManageEventHostUser amehUser = userJoinService.joinAdminManageEventHostUser(serviceRequest);
        return ApiResponse.of(amehUser.getId());
    }

    //수정
    @PatchMapping("/{amehUserId}")
    @Operation(summary = "이벤트 호스트 유저 수정")
    public ApiResponse<Void> modifyAdminManageEventHostUser(
            @PathVariable(name="amehUserId") Long amehUserId,
            @RequestBody AMEHUserUpdateApiRequest apiRequest
    ) {
        UserModifyRequest userModifyRequest = apiRequest.toUserModifyRequest();
        ValidationUtil.validate(userModifyRequest);
        userInfoService.modifyAdminManageEventHostUser(
                AdminManageEventHostUserModifyRequest.builder()
                        .userModifyRequest(userModifyRequest)
                        .build(),
                amehUserId
        );
        return ApiResponse.of(null);
    }


    //삭제
    @DeleteMapping("/{amehUserId}")
    @Operation(summary = "이벤트 호스트 유저 삭제")
    public ApiResponse<Void> deleteAdminManageEventHostUser(
            @PathVariable(name="amehUserId") Long amehUserId
    ) {
        userJoinService.deleteAdminManageEventHostUser(amehUserId);
        return ApiResponse.of(null);
    }
}
