package com.BaGulBaGul.BaGulBaGul.domain.admin.user.controller;

import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/user/pwuser")
@RequiredArgsConstructor
@Api(tags = "관리자 - 사용자 관리 - 패스워드 로그인 유저 관리")
@PreAuthorize("hasAuthority('MANAGE_USER')")
public class PasswordLoginUserAdminController {
//    @PostMapping("/")
//    @Operation(summary = "패스워드 로그인 유저 등록")
//    public ApiResponse<Long> registe      rPasswordLoginUser(
//            @RequestBody
//    )
}
