package com.BaGulBaGul.BaGulBaGul.domain.user.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.OtherUserInfoResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtherUserInfoApiResponse {
    @ApiModelProperty(value = "유저 id")
    Long id;
    @ApiModelProperty(value = "닉네임")
    String nickname;
    @ApiModelProperty(value = "이메일")
    String email;
    @ApiModelProperty(value = "프로필 상태 메세지")
    String profileMessage;
    @ApiModelProperty(value = "프로필 이미지")
    String imageURI;
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
