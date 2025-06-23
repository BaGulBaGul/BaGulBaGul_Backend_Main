package com.BaGulBaGul.BaGulBaGul.domain.user.controller;

import com.BaGulBaGul.BaGulBaGul.domain.user.SocialLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.SocialLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtCookieService;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtProvider;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.api.response.CheckDuplicateUsernameApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.api.request.SocialLoginUserJoinApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.validation.ValidationUtil;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/user/join")
@RequiredArgsConstructor
@Api(tags = "유저 회원가입")
public class UserJoinControllerImpl implements UserJoinController {

    private final UserJoinService userJoinService;
    private final JwtCookieService jwtCookieService;
    private final JwtProvider jwtProvider;

    @Override
    @PostMapping("/social")
    @Operation(summary = "소셜 로그인 유저 회원가입 요청",
            description = "redirect시에 프론트에 넘긴 joinToken은 필수\n"
                    + "닉네임도 필수. 이메일은 선택사항"
                    + "회원가입 성공 시 즉시 로그인 됨(인증 토큰을 쿠키에 저장)"
    )
    public ApiResponse<Object> joinSocialLoginUser(
            @RequestBody SocialLoginUserJoinApiRequest socialLoginUserJoinApiRequest,
            HttpServletResponse response
    ) {
        SocialLoginUserJoinRequest socialLoginUserJoinRequest = socialLoginUserJoinApiRequest
                .toSocialLoginUserJoinRequest();
        ValidationUtil.validate(socialLoginUserJoinRequest);

        SocialLoginUser user = userJoinService.registerSocialLoginUser(socialLoginUserJoinRequest);

        jwtCookieService.setAccessToken(response, jwtProvider.createAccessToken(user.getUser().getId()));
        jwtCookieService.setRefreshToken(response, jwtProvider.createRefreshToken(user.getUser().getId()));
        return ApiResponse.of(null);
    }

    @Override
    @DeleteMapping("")
    @Operation(summary = "회원탈퇴 요청",
            description = "회원탈퇴 후 인증 토큰 쿠키 삭제"
    )
    public ApiResponse<Object> deleteUser(
            @AuthenticationPrincipal Long userId,
            HttpServletResponse response
    ) {
        userJoinService.deleteUser(userId);
        jwtCookieService.deleteAccessToken(response);
        jwtCookieService.deleteRefreshToken(response);
        return ApiResponse.of(null);
    }

    @Override
    @GetMapping("/check-duplicate-username")
    @Operation(summary = "유저명 중복 체크 api")
    public ApiResponse<CheckDuplicateUsernameApiResponse> checkDuplicateUsername(
            String username
    ) {
        return ApiResponse.of(
                CheckDuplicateUsernameApiResponse.builder()
                        .duplicate(userJoinService.checkDuplicateUsername(username))
                        .build()
        );
    }
}
