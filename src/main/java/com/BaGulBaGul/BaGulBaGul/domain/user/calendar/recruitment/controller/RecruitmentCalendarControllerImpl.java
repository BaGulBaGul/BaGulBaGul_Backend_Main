package com.BaGulBaGul.BaGulBaGul.domain.user.calendar.recruitment.controller;

import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.recruitment.dto.RecruitmentCalendarExistsResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.recruitment.dto.RecruitmentCalendarSearchRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.recruitment.dto.RecruitmentCalendarSearchResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.recruitment.service.RecruitmentCalendarService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/calendar/recruitment")
@RequiredArgsConstructor
@Api(tags = "캘린더")
public class RecruitmentCalendarControllerImpl implements RecruitmentCalendarController {

    private final RecruitmentCalendarService recruitmentCalendarService;

    @Override
    @GetMapping("")
    @Operation(summary = "캘린더에 등록된 모집글 중 검색 시작 시간과 검색 종료 시간 사이에 있는 모집글 정보를 검색",
            description = "로그인 필요"
    )
    public ApiResponse<List<RecruitmentCalendarSearchResponse>> searchRecruitmentByCondition(
            @AuthenticationPrincipal Long userId,
            RecruitmentCalendarSearchRequest recruitmentCalendarSearchRequest)
    {
        return ApiResponse.of(
                recruitmentCalendarService.findRecruitmentCalendarByCondition(
                    userId,
                    recruitmentCalendarSearchRequest
                )
        );
    }

    @Override
    @GetMapping("/{recruitmentId}/exists")
    @Operation(summary = "캘린더에 특정 모집글이 존재하는지 확인",
            description = "로그인 필요"
    )
    public ApiResponse<RecruitmentCalendarExistsResponse> existsRecruitmentCalendar(
            @AuthenticationPrincipal Long userId,
            @PathVariable("recruitmentId") Long recruitmentId
    ) {
        return ApiResponse.of(
                RecruitmentCalendarExistsResponse.builder()
                        .exists(recruitmentCalendarService.existsRecruitmentCalendar(userId, recruitmentId))
                        .build()
        );
    }

    @Override
    @PostMapping("/{recruitmentId}")
    @Operation(summary = "캘린더에 모집글을 등록",
            description = "로그인 필요"
    )
    public ApiResponse<Object> registerRecruitmentCalendar(
            @AuthenticationPrincipal Long userId,
            @PathVariable("recruitmentId") Long recruitmentId
    ) {
        recruitmentCalendarService.registerRecruitmentCalendar(userId, recruitmentId);
        return ApiResponse.of(null);
    }

    @Override
    @DeleteMapping("/{recruitmentId}")
    @Operation(summary = "캘린더에서 모집글을 삭제",
            description = "로그인 필요"
    )
    public ApiResponse<Object> deleteRecruitmentCalendar(
            @AuthenticationPrincipal Long userId,
            @PathVariable("recruitmentId") Long recruitmentId
    ) {
        recruitmentCalendarService.deleteRecruitmentCalendar(userId, recruitmentId);
        return ApiResponse.of(null);
    }
}
