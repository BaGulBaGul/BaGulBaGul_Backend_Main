package com.BaGulBaGul.BaGulBaGul.domain.user.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.OtherUserInfoResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class OtherUserInfoApiResponse extends UserInfoApiResponse {
    @ApiModelProperty(value = "작성한 게시글 개수")
    long writingCount;

    public static OtherUserInfoApiResponse from(OtherUserInfoResponse myUserInfoResponse) {
        return OtherUserInfoApiResponse.builder()
                .id(myUserInfoResponse.getId())
                .nickname(myUserInfoResponse.getNickname())
                .email(myUserInfoResponse.getEmail())
                .profileMessage(myUserInfoResponse.getProfileMessage())
                .imageURI(myUserInfoResponse.getImageURI())
                .writingCount(myUserInfoResponse.getWritingCount())
                .build();
    }
}
