package com.BaGulBaGul.BaGulBaGul.domain.admin.user.controller;

import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.AMEHUserRegisterApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.AMEHUserUpdateApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.response.AMEHUserRegisterApiResponse;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/user/amehuser")
@RequiredArgsConstructor
@Api(tags = "관리자 - 사용자 관리 - 관리자 관리 이벤트 호스트 유저(AdminManageEventHostUser = AMEHUser) 관리")
@PreAuthorize("hasAuthority('MANAGE_USER')")
public class AMEHUserAdminController {

    private final UserJoinService userJoinService;
    private final UserInfoService userInfoService;
    private final UserAdminService userAdminService;

    //등록
    @PostMapping("/")
    @Operation(summary = "관리자 관리 이벤트 호스트 유저 등록")
    public ApiResponse<AMEHUserRegisterApiResponse> registerAdminManageEventHostUser(
            @RequestBody AMEHUserRegisterApiRequest apiRequest
    ) {
        AdminManageEventHostUserJoinRequest serviceRequest = apiRequest.toServiceRequest();
        ValidationUtil.validate(serviceRequest);
        Long userId = userAdminService.registerAMEHUserAndGetUserId(serviceRequest);
        return ApiResponse.of(
                AMEHUserRegisterApiResponse.builder()
                        .userId(userId)
                        .build()
        );
    }

    //수정
    @PatchMapping("/{userId}")
    @Operation(summary = "관리자 관리 이벤트 호스트 유저 수정")
    public ApiResponse<Void> modifyAdminManageEventHostUser(
            @PathVariable(name="userId") Long userId,
            @RequestBody AMEHUserUpdateApiRequest apiRequest
    ) {
        UserModifyRequest userModifyRequest = apiRequest.toUserModifyRequest();
        ValidationUtil.validate(userModifyRequest);
        userInfoService.modifyAdminManageEventHostUser(
                AdminManageEventHostUserModifyRequest.builder()
                        .userModifyRequest(userModifyRequest)
                        .build(),
                userId
        );
        return ApiResponse.of(null);
    }


    //삭제
    @DeleteMapping("/{userId}")
    @Operation(summary = "관리자 관리 이벤트 호스트 유저 삭제")
    public ApiResponse<Void> deleteAdminManageEventHostUser(
            @PathVariable(name="userId") Long userId
    ) {
        userJoinService.deleteAdminManageEventHostUser(userId);
        return ApiResponse.of(null);
    }
}
