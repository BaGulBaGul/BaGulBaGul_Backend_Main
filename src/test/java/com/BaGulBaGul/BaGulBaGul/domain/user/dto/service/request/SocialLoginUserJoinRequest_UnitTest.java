package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.SocialLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.SocialLoginUserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.global.validation.ValidationUtil;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class SocialLoginUserJoinRequest_UnitTest {
    @Nested
    @DisplayName("검증 테스트")
    class Validation {
        @Test
        @DisplayName("정상 동작")
        void shouldOk() {
            SocialLoginUserJoinRequest socialLoginUserJoinRequest = SocialLoginUserSample
                    .getNormalSocialLoginUserJoinRequest();
            assertDoesNotThrow(() -> ValidationUtil.validate(socialLoginUserJoinRequest));
        }

        @Test
        @DisplayName("내부 dto를 검증하는지 테스트")
        void shouldThrowConstraintViolationException_whenInnerDtoIsInvalid() {
            SocialLoginUserJoinRequest socialLoginUserJoinRequest = SocialLoginUserSample
                    .getNormalSocialLoginUserJoinRequest();
            socialLoginUserJoinRequest.setUserRegisterRequest(UserSample.getUnNormalUserRegisterRequest());
            assertThatThrownBy(() -> ValidationUtil.validate(socialLoginUserJoinRequest))
                    .isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("jointoken이 null이면 예외")
        void shouldThrowConstraintViolationException_whenJoinTokenIsNull() {
            SocialLoginUserJoinRequest socialLoginUserJoinRequest = SocialLoginUserSample
                    .getNormalSocialLoginUserJoinRequest();
            socialLoginUserJoinRequest.setJoinToken(null);
            assertThatThrownBy(() -> ValidationUtil.validate(socialLoginUserJoinRequest))
                    .isInstanceOf(ConstraintViolationException.class);
        }
    }
}
