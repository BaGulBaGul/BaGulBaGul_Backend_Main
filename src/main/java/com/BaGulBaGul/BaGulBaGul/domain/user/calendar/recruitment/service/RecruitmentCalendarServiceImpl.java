package com.BaGulBaGul.BaGulBaGul.domain.user.calendar.recruitment.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.exception.RecruitmentNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.RecruitmentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.RecruitmentCalendar;
import com.BaGulBaGul.BaGulBaGul.domain.user.RecruitmentCalendar.RecruitmentCalendarId;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.exception.CalendarNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.exception.DuplicateCalendarException;
import com.BaGulBaGul.BaGulBaGul.domain.user.calendar.recruitment.repository.RecruitmentCalendarRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.exception.UserNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecruitmentCalendarServiceImpl implements RecruitmentCalendarService {

    private final UserRepository userRepository;
    private final RecruitmentCalendarRepository recruitmentCalendarRepository;
    private final RecruitmentRepository recruitmentRepository;

    @Override
    public boolean existsRecruitmentCalendar(Long userId, Long recruitmentId) {
        return recruitmentCalendarRepository.existsById(
                new RecruitmentCalendarId(userId, recruitmentId)
        );
    }

    @Override
    @Transactional
    public void registerRecruitmentCalendar(Long userId, Long recruitmentId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow(() -> new RecruitmentNotFoundException());
        if(recruitment.getDeleted()) {
            throw new RecruitmentNotFoundException();
        }
        recruitmentCalendarRepository.save(
                new RecruitmentCalendar(user, recruitment)
        );
    }

    @Override
    @Transactional
    public void deleteRecruitmentCalendar(Long userId, Long recruitmentId) {
        recruitmentCalendarRepository.deleteById(
                new RecruitmentCalendarId(userId, recruitmentId)
        );
    }
}
