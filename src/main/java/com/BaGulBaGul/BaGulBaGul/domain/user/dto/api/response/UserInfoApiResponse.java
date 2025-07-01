package com.BaGulBaGul.BaGulBaGul.domain.user.dto.api.response;


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
public abstract class UserInfoApiResponse {
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
}
