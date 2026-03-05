package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request;

import com.BaGulBaGul.BaGulBaGul.global.validation.ValidationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SuspendUserRequest_UnitTest {

    @Test
    @DisplayName("성공 케이스")
    void success() {
        //given
        SuspendUserRequest request = new SuspendUserRequest(
                "reason",
                LocalDateTime.now().plusDays(7)
        );
        //when
        //then
        assertDoesNotThrow(() -> ValidationUtil.validate(request));
    }

    @Test
    @DisplayName("reason이 null이면 예외 발생")
    void test_null_reason() {
        //given
        SuspendUserRequest request = new SuspendUserRequest(
                null,
                LocalDateTime.now().plusDays(7).withNano(0)
        );
        //when
        //then
        assertThatThrownBy(() -> ValidationUtil.validate(request))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("endDate가 null이면 예외 발생")
    void test_null_endDate() {
        //given
        SuspendUserRequest request = new SuspendUserRequest(
                "reason",
                null
        );
        //when
        //then
        assertThatThrownBy(() -> ValidationUtil.validate(request))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("endDate가 과거면 예외 발생")
    void test_past_endDate() {
        //given
        SuspendUserRequest request = new SuspendUserRequest(
                "reason",
                LocalDateTime.now().minusSeconds(1).withNano(0)
        );
        //when
        //then
        assertThatThrownBy(() -> ValidationUtil.validate(request))
                .isInstanceOf(ConstraintViolationException.class);
    }
}