package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordLoginUserRegisterRequest {
    @NotNull
    @Pattern(regexp = "^[\\S]+$", message = "로그인 id에 공백은 사용할 수 없습니다.")
    @Size(min = 6, max = 20, message = "로그인 id 길이는 {2}이상 {1}이하여야 합니다")
    String loginId;

    @NotNull
    @Pattern(regexp = "^[\\S]+$", message = "로그인 password에 공백은 사용할 수 없습니다.")
    @Size(min = 6, max = 20, message = "로그인 password 길이는 {2}이상 {1}이하여야 합니다")
    String loginPassword;
}
