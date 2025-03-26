package com.BaGulBaGul.BaGulBaGul.domain.common.dto.request;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.BaGulBaGul.BaGulBaGul.domain.common.sampledata.PeriodSample;
import com.BaGulBaGul.BaGulBaGul.domain.post.sampledata.PostSample;
import com.BaGulBaGul.BaGulBaGul.global.validation.ValidationUtil;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PeriodRegisterRequest_UnitTest {
    @Nested
    @DisplayName("검증 테스트")
    class Validation {
        @Test
        @DisplayName("정상")
        void shouldOK() {
            assertDoesNotThrow(() -> ValidationUtil.validate(PeriodSample.getNormalRegisterRequest()));
        }

        @Test
        @DisplayName("startDate는 null 가능")
        void shouldOK_WhenStartDateIsNull() {
            PeriodRegisterRequest periodRegisterRequest = PeriodSample.getNormalRegisterRequest();
            periodRegisterRequest.setStartDate(null);
            assertDoesNotThrow(() -> ValidationUtil.validate(periodRegisterRequest));
        }

        @Test
        @DisplayName("endDate는 null 가능")
        void shouldOK_WhenEndDateIsNull() {
            PeriodRegisterRequest periodRegisterRequest = PeriodSample.getNormalRegisterRequest();
            periodRegisterRequest.setEndDate(null);
            assertDoesNotThrow(() -> ValidationUtil.validate(periodRegisterRequest));
        }

        @Test
        @DisplayName("startDate가 endDate보다 이후에 있으면 예외")
        void shouldThrowConstraintViolationException_WhenStartDateAfterEndDate() {
            PeriodRegisterRequest periodRegisterRequest = PeriodSample.getNormalRegisterRequest();
            periodRegisterRequest.setStartDate(periodRegisterRequest.getEndDate().plusHours(1L));
            assertThatThrownBy(() -> ValidationUtil.validate(periodRegisterRequest))
                    .isInstanceOf(ConstraintViolationException.class);
        }
    }

}