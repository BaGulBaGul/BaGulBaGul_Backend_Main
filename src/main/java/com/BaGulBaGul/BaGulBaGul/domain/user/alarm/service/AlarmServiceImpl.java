package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service;


import com.BaGulBaGul.BaGulBaGul.domain.user.Alarm;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.dto.AlarmPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.exception.AlarmNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.exception.DuplicateAlarmCheckException;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.repository.AlarmRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.repository.UserAlarmStatusRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.service.creator.AlarmCreator;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.global.alarm.realtime.RealtimeAlarmPublishService;
import com.BaGulBaGul.BaGulBaGul.global.exception.NoPermissionException;
import javax.persistence.OptimisticLockException;
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
    private final UserAlarmStatusRepository userAlarmStatusRepository;

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
                        .subject(alarmCreator.getSubject())
                        .time(alarmCreator.getTime())
                        .build()
        );
        //유저의 알람 통계 테이블 업데이트
        //총 개수와 체크하지 않은 알람 개수를 1씩 증가시킴
        userAlarmStatusRepository.increaseTotalAndUnchecked(targetUser.getId());
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
        Page<Alarm> alarms = alarmRepository.findAlarmPageOrderByTime(userId, pageable);
        return alarms.map(AlarmPageResponse::of);
    }


    @Override
    @Transactional
    public void checkAlarm(Long userId, Long alarmId) {
        //알람을 검색
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(() -> new AlarmNotFoundException());
        //알람을 체크할 권한이 없다면 예외
        if(!userId.equals(alarm.getUser().getId())) {
            throw new NoPermissionException();
        }
        //만약 이미 체크된 알람이라면 예외
        if(alarm.isChecked()) {
            throw new DuplicateAlarmCheckException();
        }
        //알람 체크, optimistic lock이 적용되어 있음
        alarm.setChecked(true);
        //유저의 알람 통계 테이블 업데이트
        //체크하지 않은 알람 개수를 1 감소
        userAlarmStatusRepository.decreaseUnchecked(userId);
    }

    @Override
    @Transactional
    public void deleteAlarm(Long userId, Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(() -> new AlarmNotFoundException());
        //알람을 삭제할 권한이 있는지
        if(!userId.equals(alarm.getUser().getId())) {
            throw new NoPermissionException();
        }
        //알람 삭제
        int deletedCnt = alarmRepository.deleteByAlarmIdAndVersion(alarmId, alarm.getVersion());
        //다른 트랜젝션에서 이미 삭제했거나 알람을 수정했음(낙관적 락 version 확인). rollback
        if(deletedCnt == 0) {
            throw new OptimisticLockException();
        }
        //유저의 알람 통계 테이블 업데이트
        //체크된 알람인 경우 total만 감소
        if(alarm.isChecked()) {
            userAlarmStatusRepository.decreaseTotal(userId);
        }
        //체크되지 않은 알람인 경우 total과 unchecked 감소
        else {
            userAlarmStatusRepository.decreaseTotalAndUnchecked(userId);
        }
    }

    @Override
    @Transactional
    public void deleteAllAlarm(Long userId) {
        alarmRepository.deleteAllByUserId(userId);
        userAlarmStatusRepository.resetStatus(userId);
    }
}
