package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.constant.UserSuspensionActionType;
import com.BaGulBaGul.BaGulBaGul.domain.user.UserSuspensionLog;
import com.BaGulBaGul.BaGulBaGul.domain.user.UserSuspensionStatus;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.LiftUserSuspensionRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.SuspendUserRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.UserSuspensionStatusResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.UserNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.UserNotSuspendedException;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserSuspensionLogRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserSuspensionStatusRepository;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.GeneralRoleType;
import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode;
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

    private final UserRoleService userRoleService;

    @Override
    @Transactional
    public void suspendUser(Long adminId, Long userId, SuspendUserRequest suspendUserRequest) {
        //admin 보호
        checkAdmin(userId);

        LocalDateTime currentTime = LocalDateTime.now();

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        User admin = userRepository.findById(adminId).orElseThrow(UserNotFoundException::new);

        //유저 정지의 상태를 확인
        UserSuspensionStatus userSuspensionStatus = userSuspensionStatusRepository.findById(userId).orElse(null);
        //없다면 생성
        if (userSuspensionStatus == null) {
            userSuspensionStatus = new UserSuspensionStatus(user, null, null);
        }

        //이미 정지된 상태라면 업데이트
        if (getUserSuspensionStatus(userId).isSuspended()) {
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
        //admin 보호
        checkAdmin(userId);

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        User admin = userRepository.findById(adminId).orElseThrow(UserNotFoundException::new);

        if (!getUserSuspensionStatus(userId).isSuspended()) {
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
    @Transactional
    public UserSuspensionStatusResponse getUserSuspensionStatus(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        //현재 정지 상태가 아니라면 false 반환
        if(!user.isSuspended()) {
            return new UserSuspensionStatusResponse(false, null, null);
        }
        //정지 상태 검색
        UserSuspensionStatus userSuspensionStatus = userSuspensionStatusRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User is suspended but has no suspension status"));
        //현재 시간이 정지 기한 이전이라면 true 반환
        if(LocalDateTime.now().isBefore(userSuspensionStatus.getEndDate())) {
            return new UserSuspensionStatusResponse(
                    true,
                    userSuspensionStatus.getEndDate(),
                    userSuspensionStatus.getReason()
            );
        }
        //현재 정지 상태이면서 현재 시간이 정지 기한을 넘겼다면 정지 플래그를 해제
        else {
            user.setSuspended(false);
            userSuspensionStatus.setEndDate(null);
            userSuspensionStatus.setReason(null);
            return new UserSuspensionStatusResponse(false, null, null);
        }
    }

    private void checkAdmin(Long userId) {
        if(userRoleService.hasRole(userId, GeneralRoleType.ADMIN.name())) {
            throw new GeneralException(ResponseCode.FORBIDDEN);
        };
    }
}
