package com.BaGulBaGul.BaGulBaGul.domain.user.dto.api.response;


import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.UserInfoResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoApiResponse {
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

    public static UserInfoApiResponse from(UserInfoResponse userInfoResponse) {
        return UserInfoApiResponse.builder()
                .id(userInfoResponse.getId())
                .nickname(userInfoResponse.getNickname())
                .email(userInfoResponse.getEmail())
                .profileMessage(userInfoResponse.getProfileMessage())
                .imageURI(userInfoResponse.getImageURI())
                .build();
    }
}
