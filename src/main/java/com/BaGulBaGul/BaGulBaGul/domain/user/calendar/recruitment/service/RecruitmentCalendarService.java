package com.BaGulBaGul.BaGulBaGul.domain.user.calendar.recruitment.service;

public interface RecruitmentCalendarService {
    boolean existsRecruitmentCalendar(Long userId, Long recruitmentId);
    void registerRecruitmentCalendar(Long userId, Long recruitmentId);
    void deleteRecruitmentCalendar(Long userId, Long recruitmentId);
}
