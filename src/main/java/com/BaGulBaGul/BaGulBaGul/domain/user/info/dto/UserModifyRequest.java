package com.BaGulBaGul.BaGulBaGul.domain.user.info.dto;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import io.swagger.annotations.ApiModelProperty;
import java.util.regex.Pattern;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserModifyRequest {
    @ApiModelProperty(value = "이메일")
    @Email(message = "이메일 형식이 아닙니다.")
    JsonNullable<String> email = JsonNullable.undefined();

    @ApiModelProperty(value = "프로필 상태 메세지")
    JsonNullable<String> profileMessage = JsonNullable.undefined();

    @ApiModelProperty(value = "프로필 이미지의 resource id")
    JsonNullable<Long> imageResourceId = JsonNullable.undefined();

    @ApiModelProperty(value = "유저명")
    JsonNullable<String> username = JsonNullable.undefined();

    private static Pattern USERNAME_PATTERN = Pattern.compile("^[가-힣a-zA-Z]{2,12}$");

    @AssertTrue(message = "유저명은 2이상 12이하의 영어, 한글 문자여야 합니다.")
    public boolean isUsernameValid() {
        if(username.isPresent()) {
            if(username == null) {
                return false;
            }
            return USERNAME_PATTERN.matcher(username.get()).matches();
        }
        return true;
    }
}
