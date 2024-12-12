package com.BaGulBaGul.BaGulBaGul.domain.common.dto.request;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.BaGulBaGul.BaGulBaGul.domain.common.sampledata.ParticipantStatusSample;
import com.BaGulBaGul.BaGulBaGul.global.validation.ValidationUtil;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ParticipantStatusRegisterRequest_UnitTest {

    @Nested
    @DisplayName("검증 테스트")
    class Validation{
        @Test
        @DisplayName("정상")
        void shouldOK() {
            assertDoesNotThrow(() -> ValidationUtil.validate(ParticipantStatusSample.getNormalRegisterRequest()));
        }

        @Test
        @DisplayName("currentHeadCount는 null을 허용")
        void shouldOK_WhenCurrentHeadCountIsNull() {
            ParticipantStatusRegisterRequest registerRequest = ParticipantStatusSample.getNormalRegisterRequest();
            registerRequest.setCurrentHeadCount(null);
            assertDoesNotThrow(() -> ValidationUtil.validate(registerRequest));
        }

        @Test
        @DisplayName("maxHeadCount는 null을 허용")
        void shouldOK_WhenMaxHeadCountIsNull() {
            ParticipantStatusRegisterRequest registerRequest = ParticipantStatusSample.getNormalRegisterRequest();
            registerRequest.setMaxHeadCount(null);
            assertDoesNotThrow(() -> ValidationUtil.validate(registerRequest));
        }

        @Test
        @DisplayName("currentHeadCount가 0이상이 아니면 예외")
        void shouldThrowConstraintViolationException_WhenCurrentHeadCountGreaterThan0() {
            ParticipantStatusRegisterRequest registerRequest = ParticipantStatusSample.getNormalRegisterRequest();
            registerRequest.setCurrentHeadCount(-1);
            assertThatThrownBy(() -> ValidationUtil.validate(registerRequest))
                    .isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("maxHeadCount가 1이상이 아니면 예외")
        void shouldThrowConstraintViolationException_WhenMaxHeadCountGreaterThan0() {
            ParticipantStatusRegisterRequest registerRequest = ParticipantStatusSample.getNormalRegisterRequest();
            registerRequest.setMaxHeadCount(0);
            assertThatThrownBy(() -> ValidationUtil.validate(registerRequest))
                    .isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("currentHeadCount가 maxHeadCount보다 크면 예외")
        void shouldThrowConstraintViolationException_WhenCurrentHeadCountGreaterThanMaxHeadCount() {
            ParticipantStatusRegisterRequest registerRequest = ParticipantStatusSample.getNormalRegisterRequest();
            registerRequest.setCurrentHeadCount(6);
            registerRequest.setMaxHeadCount(5);
            assertThatThrownBy(() -> ValidationUtil.validate(registerRequest))
                    .isInstanceOf(ConstraintViolationException.class);
        }
    }
}