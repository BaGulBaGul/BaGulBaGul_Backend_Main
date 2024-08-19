package com.BaGulBaGul.BaGulBaGul.domain.user.calendar.recruitment.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.recruitment.dto.RecruitmentCalendarSearchRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.recruitment.dto.RecruitmentCalendarSearchResponse;
import java.util.List;

public interface RecruitmentCalendarService {
    List<RecruitmentCalendarSearchResponse> findRecruitmentCalendarByCondition(Long userId, RecruitmentCalendarSearchRequest recruitmentCalendarSearchRequest);
    boolean existsRecruitmentCalendar(Long userId, Long recruitmentId);
    void registerRecruitmentCalendar(Long userId, Long recruitmentId);
    void deleteRecruitmentCalendar(Long userId, Long recruitmentId);
}
