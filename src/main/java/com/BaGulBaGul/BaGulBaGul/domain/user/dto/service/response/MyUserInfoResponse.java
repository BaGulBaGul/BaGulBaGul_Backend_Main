package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
public class MyUserInfoResponse extends UserInfoResponse {
    long writingCount;
    long postLikeCount;
    long calendarCount;

    public static MyUserInfoResponse from(
            UserInfoResponse userInfoResponse,
            long writingCount,
            long postLikeCount,
            long calendarCount
    ) {
        return userInfoResponse.mapBuilder(MyUserInfoResponse.builder())
                .writingCount(writingCount)
                .postLikeCount(postLikeCount)
                .calendarCount(calendarCount)
                .build();
    }
}
