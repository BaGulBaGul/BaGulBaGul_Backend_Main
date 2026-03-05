package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    Long id;
    String nickname;
    String email;
    String profileMessage;
    String imageURI;

    public <T extends UserInfoResponseBuilder> T mapBuilder(T builder) {
        return (T) builder
                .id(id)
                .nickname(nickname)
                .email(email)
                .profileMessage(profileMessage)
                .imageURI(imageURI);
    }

    public static UserInfoResponse from(User user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileMessage(user.getProfileMessage())
                .imageURI(user.getImageURI())
                .build();
    }
}
