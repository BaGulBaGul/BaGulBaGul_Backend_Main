package com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.BaGulBaGul.BaGulBaGul.domain.event.sampledata.EventSample;
import com.BaGulBaGul.BaGulBaGul.global.validation.ValidationUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class EventRegisterRequest_UnitTest {

    @Nested
    @DisplayName("검증 테스트")
    class Validation {
        //Event의 책임이 아닌 부분은 검증 대상에서 제외
        EventRegisterRequest getNormalRegisterRequest() {
            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(1L);
            eventRegisterRequest.setLocationRegisterRequest(null);
            eventRegisterRequest.setParticipantStatusRegisterRequest(null);
            eventRegisterRequest.setPeriodRegisterRequest(null);
            eventRegisterRequest.setPostRegisterRequest(null);
            return eventRegisterRequest;
        }

        @Test
        @DisplayName("정상 동작")
        void shouldOK() {
            assertDoesNotThrow(() -> ValidationUtil.validate(getNormalRegisterRequest()));
        }

        @Test
        @DisplayName("EventType이 null인 경우 예외")
        void shouldThrowConstraintViolationException_WhenEventTypeIsNull() {
            EventRegisterRequest eventRegisterRequest = getNormalRegisterRequest();
            eventRegisterRequest.setType(null);
            assertThatThrownBy(() -> ValidationUtil.validate(eventRegisterRequest))
                    .isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("ageLimit가 null인 경우 예외")
        void shouldThrowConstraintViolationException_WhenAgeLimitIsNull() {
            EventRegisterRequest eventRegisterRequest = getNormalRegisterRequest();
            eventRegisterRequest.setAgeLimit(null);
            assertThatThrownBy(() -> ValidationUtil.validate(eventRegisterRequest))
                    .isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("카테고리 개수가 한계를 넘는 경우 예외")
        void shouldThrowConstraintViolationException_WhenCategoryCountOver() {
            EventRegisterRequest eventRegisterRequest = getNormalRegisterRequest();
            List<String> categories = new ArrayList<>(
                    Collections.nCopies(EventSample.CATEGORY_MAX_COUNT + 1, "a"));
            eventRegisterRequest.setCategories(categories);
            assertThatThrownBy(() -> ValidationUtil.validate(eventRegisterRequest))
                    .isInstanceOf(ConstraintViolationException.class);
        }
    }
}