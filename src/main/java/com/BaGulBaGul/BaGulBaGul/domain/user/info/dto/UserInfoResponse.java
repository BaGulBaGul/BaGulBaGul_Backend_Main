package com.BaGulBaGul.BaGulBaGul.domain.user.info.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class UserInfoResponse {
    Long id;
    String nickname;
    String email;
    String profileMessage;
    String imageURI;
}
