package com.BaGulBaGul.BaGulBaGul.domain.calendar.recruitment.controller;

import com.BaGulBaGul.BaGulBaGul.domain.calendar.recruitment.dto.RecruitmentCalendarExistsResponse;
import com.BaGulBaGul.BaGulBaGul.domain.calendar.recruitment.dto.RecruitmentCalendarSearchRequest;
import com.BaGulBaGul.BaGulBaGul.domain.calendar.recruitment.dto.RecruitmentCalendarSearchResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import java.util.List;

public interface RecruitmentCalendarController {
    ApiResponse<List<RecruitmentCalendarSearchResponse>> searchRecruitmentByCondition(Long userId, RecruitmentCalendarSearchRequest recruitmentCalendarSearchRequest);
    ApiResponse<RecruitmentCalendarExistsResponse> existsRecruitmentCalendar(Long userId, Long recruitmentId);
    ApiResponse<Object> registerRecruitmentCalendar(Long userId, Long recruitmentId);
    ApiResponse<Object> deleteRecruitmentCalendar(Long userId, Long recruitmentId);
}
