package com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.service.response;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.UserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.UserSuspensionStatusResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class UserSearchByAdminResponse extends UserInfoResponse {
    LocalDateTime joinDate;
    List<String> roles;
    boolean isSuspend;
    LocalDateTime suspendEndDate;
    String suspensionReason;

    public static UserSearchByAdminResponse from(User user, UserSuspensionStatusResponse userSuspensionStatusResponse) {
        return UserSearchByAdminResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .joinDate(user.getCreatedAt())
                .roles(user.getUserRoles().stream()
                        .map(userRole -> userRole.getRole().getName())
                        .collect(Collectors.toList()))
                .isSuspend(userSuspensionStatusResponse.isSuspended())
                .suspendEndDate(userSuspensionStatusResponse.getEndDate())
                .suspensionReason(userSuspensionStatusResponse.getReason())
                .build();
    }
}
