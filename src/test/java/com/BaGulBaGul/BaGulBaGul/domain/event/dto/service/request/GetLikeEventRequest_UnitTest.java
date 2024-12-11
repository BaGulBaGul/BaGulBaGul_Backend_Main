package com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.BaGulBaGul.BaGulBaGul.global.validation.ValidationUtil;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class GetLikeEventRequest_UnitTest {
    @Nested
    @DisplayName("검증 테스트")
    class Validation {
        @Test
        @DisplayName("EventType이 null이면 예외")
        void shouldThrowConstraintViolationException_WhenEventTypeIsNull() {
            assertThatThrownBy(() -> ValidationUtil.validate(new GetLikeEventRequest(null)))
                    .isInstanceOf(ConstraintViolationException.class);
        }
    }
}