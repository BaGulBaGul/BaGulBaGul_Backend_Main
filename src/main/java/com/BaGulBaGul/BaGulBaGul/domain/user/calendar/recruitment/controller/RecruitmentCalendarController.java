package com.BaGulBaGul.BaGulBaGul.domain.user.calendar.recruitment.controller;

import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.recruitment.dto.RecruitmentCalendarExistsResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;

public interface RecruitmentCalendarController {
    ApiResponse<RecruitmentCalendarExistsResponse> existsRecruitmentCalendar(Long userId, Long recruitmentId);
    ApiResponse<Object> registerRecruitmentCalendar(Long userId, Long recruitmentId);
    ApiResponse<Object> deleteRecruitmentCalendar(Long userId, Long recruitmentId);
}
