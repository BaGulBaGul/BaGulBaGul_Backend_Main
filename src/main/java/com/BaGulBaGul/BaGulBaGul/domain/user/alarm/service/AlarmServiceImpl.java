package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service;


import com.BaGulBaGul.BaGulBaGul.domain.user.Alarm;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.dto.AlarmPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.exception.AlarmNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.repository.AlarmRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import com.BaGulBaGul.BaGulBaGul.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    @Override
    public Page<AlarmPageResponse> getAlarmPageByTime(Long userId, Pageable pageable) {
        User user = userRepository.getReferenceById(userId);
        Page<Alarm> alarms = alarmRepository.findAlarmPageOrderByTime(user, pageable);
        return alarms.map(AlarmPageResponse::of);
    }

    @Override
    @Transactional
    public void checkAlarm(Long userId, Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(() -> new AlarmNotFoundException());
        //알람을 체크할 권한이 있는지
        if(!userId.equals(alarm.getUser().getId())) {
            throw new GeneralException(ErrorCode.FORBIDDEN);
        }
        //알람 체크
        alarm.setChecked(true);
    }

    @Override
    @Transactional
    public void deleteAlarm(Long userId, Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(() -> new AlarmNotFoundException());
        //알람을 체크할 권한이 있는지
        if(!userId.equals(alarm.getUser().getId())) {
            throw new GeneralException(ErrorCode.FORBIDDEN);
        }
        //알람 삭제
        alarmRepository.delete(alarm);
    }

    @Override
    @Transactional
    public void deleteAllAlarm(Long userId) {
        alarmRepository.deleteAllByUserId(userId);
    }
}
