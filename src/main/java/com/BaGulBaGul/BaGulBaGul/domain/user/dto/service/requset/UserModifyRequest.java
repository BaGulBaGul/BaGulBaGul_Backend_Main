package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset;

import java.util.regex.Pattern;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.openapitools.jackson.nullable.JsonNullable;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class UserModifyRequest {

    @Builder.Default
    JsonNullable<String> email = JsonNullable.undefined();

    @Builder.Default
    JsonNullable<String> profileMessage = JsonNullable.undefined();

    @Builder.Default
    JsonNullable<Long> imageResourceId = JsonNullable.undefined();

    @Builder.Default
    JsonNullable<String> username = JsonNullable.undefined();

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[가-힣a-zA-Z]{2,12}$");
    private static final EmailValidator EMAIL_VALIDATOR = new EmailValidator();

    @AssertTrue(message = "유저명은 2이상 12이하의 영어, 한글 문자여야 합니다.")
    public boolean isUsernameValid() {
        if(username.isPresent()) {
            String usernameContent = username.get();
            //null 비허용
            if(usernameContent == null) {
                return false;
            }
            return USERNAME_PATTERN.matcher(usernameContent).matches();
        }
        return true;
    }

    @AssertTrue(message = "이메일 형식이 아닙니다.")
    public boolean isEmailValid() {
        if(email.isPresent()) {
            String emailContent = email.get();
            //null 허용
            if(emailContent == null) {
                return true;
            }
            return !emailContent.isBlank() && EMAIL_VALIDATOR.isValid(emailContent, null);
        }
        return true;
    }
}
