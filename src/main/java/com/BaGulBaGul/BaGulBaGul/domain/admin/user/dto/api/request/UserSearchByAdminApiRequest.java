package com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserSearchRequest;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.UserSubType;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSearchByAdminApiRequest {
    @ApiModelProperty("유저명")
    private String userName;
    @ApiModelProperty("회원가입일 탐색 시작 일시")
    private LocalDateTime joinDateSearchStart;
    @ApiModelProperty("회원가입일 탐색 종료 일시")
    private LocalDateTime joinDateSearchEnd;
    @ApiModelProperty("세부 타입 - 리스트 내의 세부 타입은 전부 and 처리됨 - 타입 종류는 UserSubType 참고")
    private Set<UserSubType> subTypes;
    @ApiModelProperty("역할 - 리스트 내의 역할 이름은 전부 and 처리됨")
    private Set<String> roles;

    public UserSearchRequest toUserSearchRequest() {
        return UserSearchRequest.builder()
                .userName(userName)
                .joinDateSearchStart(joinDateSearchStart)
                .joinDateSearchEnd(joinDateSearchEnd)
                .subTypes(subTypes)
                .roles(roles)
                .build();
    }
}
