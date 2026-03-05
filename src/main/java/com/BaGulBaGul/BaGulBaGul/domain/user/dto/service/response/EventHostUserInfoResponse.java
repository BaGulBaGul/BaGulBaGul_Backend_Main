package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
public class EventHostUserInfoResponse extends UserInfoResponse {
    long festivalCount;
    long localEventCount;
    long partyCount;
    LocalDateTime joinDate;

    public static EventHostUserInfoResponse from(
            UserInfoResponse userInfoResponse,
            long festivalCount,
            long localEventCount,
            long partyCount
    ) {
        return userInfoResponse.mapBuilder(EventHostUserInfoResponse.builder())
                .festivalCount(festivalCount)
                .localEventCount(localEventCount)
                .partyCount(partyCount)
                .build();
    }
}
