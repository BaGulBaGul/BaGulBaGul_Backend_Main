package com.BaGulBaGul.BaGulBaGul.domain.common.dto.request;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.BaGulBaGul.BaGulBaGul.domain.common.sampledata.ParticipantStatusSample;
import com.BaGulBaGul.BaGulBaGul.global.validation.ValidationUtil;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

class ParticipantStatusModifyRequest_UnitTest {
    @Nested
    @DisplayName("검증 테스트")
    class Validation {
        @Test
        @DisplayName("정상")
        void shouldOK() {
            assertDoesNotThrow(() -> ValidationUtil.validate(ParticipantStatusSample.getNormalModifyRequest()));
        }

        @Test
        @DisplayName("currentHeadCount가 0이상이 아니면 예외")
        void shouldThrowConstraintViolationException_WhenCurrentHeadCountGreaterThan0() {
            ParticipantStatusModifyRequest modifyRequest = ParticipantStatusSample.getNormalModifyRequest();
            modifyRequest.setCurrentHeadCount(JsonNullable.of(-1));
            assertThatThrownBy(() -> ValidationUtil.validate(modifyRequest))
                    .isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("maxHeadCount가 1이상이 아니면 예외")
        void shouldThrowConstraintViolationException_WhenMaxHeadCountGreaterThan0() {
            ParticipantStatusModifyRequest modifyRequest = ParticipantStatusSample.getNormalModifyRequest();
            modifyRequest.setMaxHeadCount(JsonNullable.of(0));
            assertThatThrownBy(() -> ValidationUtil.validate(modifyRequest))
                    .isInstanceOf(ConstraintViolationException.class);
        }
    }
}