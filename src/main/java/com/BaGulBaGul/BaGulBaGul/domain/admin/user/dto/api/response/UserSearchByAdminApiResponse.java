package com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.service.response.UserSearchByAdminResponse;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserSearchByAdminApiResponse {
    @ApiModelProperty(value = "유저 id")
    Long userId;
    @ApiModelProperty(value = "유저명")
    String username;
    @ApiModelProperty(value = "이메일")
    String email;
    @ApiModelProperty(value = "가입일")
    LocalDateTime joinDate;
    @ApiModelProperty(value = "역할명 리스트")
    List<String> roles;
    @ApiModelProperty(value = "정지 여부")
    boolean isSuspend;
    @ApiModelProperty(value = "정지 종료일")
    LocalDateTime suspendEndDate;
    @ApiModelProperty(value = "정지 이유")
    String suspensionReason;

    public static UserSearchByAdminApiResponse from(UserSearchByAdminResponse userSearchByAdminResponse) {
        return UserSearchByAdminApiResponse.builder()
                .userId(userSearchByAdminResponse.getId())
                .username(userSearchByAdminResponse.getNickname())
                .email(userSearchByAdminResponse.getEmail())
                .joinDate(userSearchByAdminResponse.getJoinDate())
                .roles(userSearchByAdminResponse.getRoles())
                .isSuspend(userSearchByAdminResponse.isSuspend())
                .suspendEndDate(userSearchByAdminResponse.getSuspendEndDate())
                .suspensionReason(userSearchByAdminResponse.getSuspensionReason())
                .build();
    }
}
