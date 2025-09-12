package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request;

import java.time.LocalDateTime;
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
}
