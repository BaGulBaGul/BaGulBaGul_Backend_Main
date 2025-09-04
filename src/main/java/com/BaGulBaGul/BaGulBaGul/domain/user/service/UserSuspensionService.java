package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.LiftUserSuspensionRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.SuspendUserRequest;

public interface UserSuspensionService {
    void suspendUser(Long adminId, Long userId, SuspendUserRequest suspendUserRequest);
    void liftSuspension(Long adminId, Long userId, LiftUserSuspensionRequest liftUserSuspensionRequest);
    boolean isUserSuspended(Long userId);
}