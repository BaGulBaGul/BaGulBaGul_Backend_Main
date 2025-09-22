package com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserSearchRequest;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.UserSubType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AMEHUserSearchByAdminApiRequest {
    private String userName;
    private LocalDateTime joinDateSearchStart;
    private LocalDateTime joinDateSearchEnd;

    private static final List<UserSubType> types = List.of(UserSubType.ADMIN_MANAGE_EVENT_HOST_USER);

    public UserSearchRequest toUserSearchRequest() {
        return UserSearchRequest.builder()
                .userName(userName)
                .joinDateSearchStart(joinDateSearchStart)
                .joinDateSearchEnd(joinDateSearchEnd)
                .subTypes(types)
                .build();
    }
}
