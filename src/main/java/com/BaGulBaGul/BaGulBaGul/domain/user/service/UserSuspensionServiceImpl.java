package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.constant.UserSuspensionActionType;
import com.BaGulBaGul.BaGulBaGul.domain.user.UserSuspensionLog;
import com.BaGulBaGul.BaGulBaGul.domain.user.UserSuspensionStatus;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.LiftUserSuspensionRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.SuspendUserRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.InvalidSuspensionRequestException;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.UserNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.UserNotSuspendedException;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserSuspensionLogRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserSuspensionStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserSuspensionServiceImpl implements UserSuspensionService {

    private final UserRepository userRepository;
    private final UserSuspensionStatusRepository userSuspensionStatusRepository;
    private final UserSuspensionLogRepository userSuspensionLogRepository;

    @Override
    @Transactional
    public void suspendUser(Long adminId, Long userId, SuspendUserRequest suspendUserRequest) {

        //정지일이 현재 이전이라면 예외.
        LocalDateTime currentTime = LocalDateTime.now();
        if(suspendUserRequest.getEndDate().isBefore(currentTime)) {
            throw new InvalidSuspensionRequestException();
        }

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        User admin = userRepository.findById(adminId).orElseThrow(UserNotFoundException::new);

        //유저 정지의 상태를 확인
        UserSuspensionStatus userSuspensionStatus = userSuspensionStatusRepository.findById(userId).orElse(null);
        //없다면 생성
        if (userSuspensionStatus == null) {
            userSuspensionStatus = new UserSuspensionStatus(user, null, null);
        }

        //이미 정지된 상태라면 업데이트
        if (user.isSuspended()) {
            userSuspensionLogRepository.save(
                    new UserSuspensionLog(user, suspendUserRequest.getReason(), currentTime, suspendUserRequest.getEndDate(), admin, UserSuspensionActionType.UPDATE)
            );
        }
        //현재 정지 상태가 아니라면 정지
        else {
            user.setSuspended(true);
            userSuspensionLogRepository.save(
                    new UserSuspensionLog(user, suspendUserRequest.getReason(), currentTime, suspendUserRequest.getEndDate(), admin, UserSuspensionActionType.SUSPEND)
            );
        }

        //현재 정지 상태 변경
        userSuspensionStatus.setEndDate(suspendUserRequest.getEndDate());
        userSuspensionStatus.setReason(suspendUserRequest.getReason());
        userSuspensionStatusRepository.save(userSuspensionStatus);
    }

    @Override
    @Transactional
    public void liftSuspension(Long adminId, Long userId, LiftUserSuspensionRequest liftUserSuspensionRequest) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        User admin = userRepository.findById(adminId).orElseThrow(UserNotFoundException::new);

        if (!user.isSuspended()) {
            throw new UserNotSuspendedException();
        }

        user.setSuspended(false);

        UserSuspensionStatus userSuspensionStatus = userSuspensionStatusRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User is suspended but has no suspension status"));

        userSuspensionStatus.setEndDate(null);
        userSuspensionStatus.setReason(null);
        userSuspensionStatusRepository.save(userSuspensionStatus);

        userSuspensionLogRepository.save(
                new UserSuspensionLog(user, liftUserSuspensionRequest.getReason(), null, null, admin, UserSuspensionActionType.LIFT)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserSuspended(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return user.isSuspended();
    }
}
