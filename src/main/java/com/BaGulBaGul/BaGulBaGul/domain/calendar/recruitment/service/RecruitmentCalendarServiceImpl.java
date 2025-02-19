package com.BaGulBaGul.BaGulBaGul.domain.calendar.recruitment.service;

import com.BaGulBaGul.BaGulBaGul.domain.calendar.recruitment.dto.RecruitmentCalendarSearchRequest;
import com.BaGulBaGul.BaGulBaGul.domain.calendar.recruitment.dto.RecruitmentCalendarSearchResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.exception.RecruitmentNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.RecruitmentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.calendar.RecruitmentCalendar;
import com.BaGulBaGul.BaGulBaGul.domain.calendar.RecruitmentCalendar.RecruitmentCalendarId;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.calendar.recruitment.repository.RecruitmentCalendarRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.UserNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecruitmentCalendarServiceImpl implements RecruitmentCalendarService {

    private final UserRepository userRepository;
    private final RecruitmentCalendarRepository recruitmentCalendarRepository;
    private final RecruitmentRepository recruitmentRepository;

    @Override
    @Transactional
    public List<RecruitmentCalendarSearchResponse> findRecruitmentCalendarByCondition(
            Long userId,
            RecruitmentCalendarSearchRequest recruitmentCalendarSearchRequest)
    {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        List<RecruitmentCalendar> recruitmentCalendarList = recruitmentCalendarRepository.findWithRecruitmentAndPostByCondition(
                user,
                recruitmentCalendarSearchRequest.getSearchStartTime(),
                recruitmentCalendarSearchRequest.getSearchEndTime()
        );
        return recruitmentCalendarList.stream().map(RecruitmentCalendarSearchResponse::of).collect(Collectors.toList());
    }

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
