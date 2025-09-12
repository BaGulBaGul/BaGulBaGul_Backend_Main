package com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserSearchRequest;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSearchByAdminApiRequest {
    private String userName;
    private LocalDateTime joinDateSearchStart;
    private LocalDateTime joinDateSearchEnd;
    public UserSearchRequest toUserSearchRequest() {
        return UserSearchRequest.builder()
                .userName(userName)
                .joinDateSearchStart(joinDateSearchStart)
                .joinDateSearchEnd(joinDateSearchEnd)
                .build();
    }
}
