package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class MyUserInfoResponse {
    Long id;
    String nickname;
    String email;
    String profileMessage;
    String imageURI;
    long writingCount;
    long postLikeCount;
    long calendarCount;
}
