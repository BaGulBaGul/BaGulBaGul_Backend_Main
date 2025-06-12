package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
public class OtherUserInfoResponse extends UserInfoResponse{
    long writingCount;

    public static OtherUserInfoResponse from(
            UserInfoResponse userInfoResponse,
            long writingCount
    ) {
        return OtherUserInfoResponse.builder()
                .id(userInfoResponse.getId())
                .nickname(userInfoResponse.getNickname())
                .email(userInfoResponse.getEmail())
                .profileMessage(userInfoResponse.getProfileMessage())
                .imageURI(userInfoResponse.getImageURI())
                .writingCount(writingCount)
                .build();
    }
}
