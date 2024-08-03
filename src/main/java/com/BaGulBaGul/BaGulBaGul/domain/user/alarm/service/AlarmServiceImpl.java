package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service;


import com.BaGulBaGul.BaGulBaGul.domain.user.Alarm;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.dto.AlarmPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.exception.AlarmNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.repository.AlarmRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.AlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.global.alarm.realtime.RealtimeAlarmPublishService;
import com.BaGulBaGul.BaGulBaGul.global.exception.NoPermissionException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;
    private final RealtimeAlarmPublishService realtimeAlarmPublishService;

    @Override
    @Transactional
    public void registerAlarm(
            AlarmCreator alarmCreator
    ) {
        User targetUser = userRepository.getReferenceById(alarmCreator.getTargetUserId());
        //알림 등록
        alarmRepository.save(
                Alarm.builder()
                        .user(targetUser)
                        .type(alarmCreator.getType())
                        .title(alarmCreator.getTitle())
                        .message(alarmCreator.getMessage())
                        .subjectId(alarmCreator.getSubject())
                        .time(alarmCreator.getTime())
                        .build()
        );
        //알림 등록 트랜젝션이 성공하면 실시간 알림을 보내도록 함
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                realtimeAlarmPublishService.publishAlarm(targetUser.getId(), alarmCreator.getTitle());
            }
        });
    }

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
            throw new NoPermissionException();
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
            throw new NoPermissionException();
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
