package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.LiftUserSuspensionRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.SuspendUserRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.UserSuspensionStatusResponse;

public interface UserSuspensionService {

    /**
     * 특정 유저를 정지한다
     * @param adminId 정지를 요청한 관리자의 id
     * @param userId 정지 대상 유저의 id
     * @param suspendUserRequest 요청 상세
     */
    void suspendUser(Long adminId, Long userId, SuspendUserRequest suspendUserRequest);

    /**
     * 특정 유저의 정지를 해제
     * @param adminId 정지 해제를 요청한 관리자의 id
     * @param userId 정지를 해제할 유저의 id
     * @param liftUserSuspensionRequest 요청 상세
     */
    void liftSuspension(Long adminId, Long userId, LiftUserSuspensionRequest liftUserSuspensionRequest);

    /**
     * 현재 유저의 정지 상태를 반환. user.isSuspended() 대신 이 메서드를 사용할 것.
     * @param userId 정지 여부를 확인 대상 유저의 id
     * @return 유저의 현재 정지 상태
     */
    UserSuspensionStatusResponse getUserSuspensionStatus(Long userId);
}