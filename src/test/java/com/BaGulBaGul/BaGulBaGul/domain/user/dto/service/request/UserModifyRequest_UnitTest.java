package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.UserModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.global.validation.ValidationUtil;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openapitools.jackson.nullable.JsonNullable;

public class UserModifyRequest_UnitTest {
    @Nested
    @DisplayName("검증 테스트")
    class Validation {
        @Test
        @DisplayName("정상 동작")
        void shouldOk() {
            assertDoesNotThrow(() -> ValidationUtil.validate(UserSample.getNormalUserModifyRequest()));
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "xxx", "xxx@", "@a", "@"})
        @DisplayName("이메일 형식이 아니라면 예외")
        void shouldThrowConstraintViolationException_whenEmailFormatIsInvalid(String email) {
            UserModifyRequest userModifyRequest = UserSample.getNormalUserModifyRequest();
            userModifyRequest.setEmail(JsonNullable.of(email));
            assertThatThrownBy(() -> ValidationUtil.validate(userModifyRequest))
                    .isInstanceOf(ConstraintViolationException.class);
        }


        @Test
        @DisplayName("유저명에 한글, 영어 이외의 문자가 포함되면 예외")
        void shouldThrowConstraintViolationException_whenUsernameContainsInvalidCharacters() {
            UserModifyRequest userModifyRequest = UserSample.getNormalUserModifyRequest();
            userModifyRequest.setUsername(JsonNullable.of("osm!!"));
            assertThatThrownBy(() -> ValidationUtil.validate(userModifyRequest))
                    .isInstanceOf(ConstraintViolationException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {"a", "aaaaaaaaaaaaa"})
        @DisplayName("유저명은 2글자 이상, 12글자 이하의 문자열이어야 한다")
        void shouldThrowConstraintViolationException_whenUsernameLengthOutOfRange(String username) {
            UserModifyRequest userModifyRequest = UserSample.getNormalUserModifyRequest();
            userModifyRequest.setUsername(JsonNullable.of(username));
            assertThatThrownBy(() -> ValidationUtil.validate(userModifyRequest))
                    .isInstanceOf(ConstraintViolationException.class);
        }

    }
}
