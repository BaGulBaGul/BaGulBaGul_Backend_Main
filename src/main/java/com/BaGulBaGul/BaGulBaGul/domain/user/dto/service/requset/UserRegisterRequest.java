package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class UserRegisterRequest {
    @Pattern(regexp = "^[가-힣a-zA-Z]+$", message = "유저명은 한글이나 영어로만 설정할 수 있습니다.")
    @Size(min = 2, max = 12, message = "유저명은 {2}이상 {1}이하의 글자여야 합니다.")
    @NotNull(message = "유저명은 필수 입력사항입니다.")
    String nickname;

    @Email(message = "이메일 형식이 아닙니다.")
    @Size(min = 1, message = "이메일은 빈 문자열일 수 없습니다")
    String email;
}
