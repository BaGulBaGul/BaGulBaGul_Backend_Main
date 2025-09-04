package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request;

import com.BaGulBaGul.BaGulBaGul.global.validation.ValidationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SuspendUserRequest_UnitTest {

    @Test
    @DisplayName("endDate가 시간 단위로 잘려있으면 성공")
    void test_endDate_truncated() {
        //given
        SuspendUserRequest request = new SuspendUserRequest(
                "reason",
                LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusDays(7)
        );
        //when
        //then
        assertDoesNotThrow(() -> ValidationUtil.validate(request));
    }

    @Test
    @DisplayName("endDate에 분이 있으면 예외 발생")
    void test_endDate_with_minutes() {
        //given
        SuspendUserRequest request = new SuspendUserRequest(
                "reason",
                LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusMinutes(1).plusDays(7)
        );
        //when
        //then
        assertThatThrownBy(() -> ValidationUtil.validate(request))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("endDate에 초가 있으면 예외 발생")
    void test_endDate_with_seconds() {
        //given
        SuspendUserRequest request = new SuspendUserRequest(
                "reason",
                LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusSeconds(1).plusDays(7)
        );
        //when
        //then
        assertThatThrownBy(() -> ValidationUtil.validate(request))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("endDate에 나노초가 있으면 예외 발생")
    void test_endDate_with_nanos() {
        //given
        SuspendUserRequest request = new SuspendUserRequest(
                "reason",
                LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusNanos(1).plusDays(7)
        );
        //when
        //then
        assertThatThrownBy(() -> ValidationUtil.validate(request))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("reason이 null이면 예외 발생")
    void test_null_reason() {
        //given
        SuspendUserRequest request = new SuspendUserRequest(
                null,
                LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusDays(7)
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
                LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).minusSeconds(1)
        );
        //when
        //then
        assertThatThrownBy(() -> ValidationUtil.validate(request))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
