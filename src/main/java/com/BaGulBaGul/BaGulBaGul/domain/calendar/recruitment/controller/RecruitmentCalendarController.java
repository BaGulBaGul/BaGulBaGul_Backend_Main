package com.BaGulBaGul.BaGulBaGul.domain.calendar.recruitment.controller;

import com.BaGulBaGul.BaGulBaGul.domain.calendar.recruitment.dto.RecruitmentCalendarExistsResponse;
import com.BaGulBaGul.BaGulBaGul.domain.calendar.recruitment.dto.RecruitmentCalendarSearchRequest;
import com.BaGulBaGul.BaGulBaGul.domain.calendar.recruitment.dto.RecruitmentCalendarSearchResponse;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import java.util.List;

public interface RecruitmentCalendarController {
    ApiResponse<List<RecruitmentCalendarSearchResponse>> searchRecruitmentByCondition(
            AuthenticatedUserInfo authenticatedUserInfo,
            RecruitmentCalendarSearchRequest recruitmentCalendarSearchRequest
    );
    ApiResponse<RecruitmentCalendarExistsResponse> existsRecruitmentCalendar(
            AuthenticatedUserInfo authenticatedUserInfo,
            Long recruitmentId
    );
    ApiResponse<Object> registerRecruitmentCalendar(
            AuthenticatedUserInfo authenticatedUserInfo,
            Long recruitmentId
    );
    ApiResponse<Object> deleteRecruitmentCalendar(
            AuthenticatedUserInfo authenticatedUserInfo,
            Long recruitmentId
    );
}
