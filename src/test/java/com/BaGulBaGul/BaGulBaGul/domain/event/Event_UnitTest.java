package com.BaGulBaGul.BaGulBaGul.domain.event;

import static org.junit.jupiter.api.Assertions.*;

import com.BaGulBaGul.BaGulBaGul.domain.event.sampledata.EventSample;
import com.BaGulBaGul.BaGulBaGul.global.validation.ValidationUtil;
import java.time.LocalDateTime;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class Event_UnitTest {
    @Nested
    @DisplayName("이벤트 엔티티 검증 테스트")
    class ValidationTest {
        @Test
        @DisplayName("정상 이벤트")
        void shouldOK() {
            assertDoesNotThrow(() -> ValidationUtil.validate(EventSample.NORMAL));
        }
        @Nested
        @DisplayName("기간 검증")
        class Period {
            @Test
            @DisplayName("시작 날짜가 종료 날짜보다 뒤에 있을 때 예외")
            void shouldThrowException_WhenStartDateLaterThanEndDate() {
                //given
                Event event = EventSample.NORMAL;
                //종료시간에 1시간을 더한 값을 시작시간으로
                event.setStartDate(event.getEndDate().plusHours(1));

                //when //then
                assertThrows(ConstraintViolationException.class, () -> ValidationUtil.validate(event));
            }
        }
        @Nested
        @DisplayName("참여자 상태 검증")
        class Participant {
            @Test
            @DisplayName("참여 인원이 모집 인원보다 많을 때 예외")
            void shouldThrowException_WhenCurrentGreaterThanMax() {
                //given
                Event event = EventSample.NORMAL;
                event.setCurrentHeadCount(event.getMaxHeadCount() + 1);

                //when //then
                assertThrows(ConstraintViolationException.class, () -> ValidationUtil.validate(event));
            }
        }
    }
}