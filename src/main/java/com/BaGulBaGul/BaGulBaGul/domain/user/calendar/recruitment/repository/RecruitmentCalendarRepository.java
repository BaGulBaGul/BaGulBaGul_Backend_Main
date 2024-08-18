package com.BaGulBaGul.BaGulBaGul.domain.user.calendar.recruitment.repository;

import com.BaGulBaGul.BaGulBaGul.domain.user.RecruitmentCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentCalendarRepository extends JpaRepository<RecruitmentCalendar, RecruitmentCalendar.RecruitmentCalendarId> {
}
