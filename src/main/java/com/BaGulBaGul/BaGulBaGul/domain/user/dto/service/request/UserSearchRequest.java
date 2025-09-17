package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request;

import com.BaGulBaGul.BaGulBaGul.global.auth.constant.UserSubType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Builder
@AllArgsConstructor
public class UserSearchRequest {
    private String userName;
    private LocalDateTime joinDateSearchStart;
    private LocalDateTime joinDateSearchEnd;
    private List<UserSubType> subTypes;
}
