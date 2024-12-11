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

class EventModifyRequest_UnitTest {
    @Nested
    @DisplayName("검증 테스트")
    class Validation {
        @Test
        @DisplayName("카테고리 개수가 한계를 넘는 경우 예외")
        void shouldThrowConstraintViolationException_WhenCategoryCountOver() {
            EventModifyRequest eventModifyRequest = EventSample.getNormalModifyRequest();
            List<String> categories = new ArrayList<>(
                    Collections.nCopies(EventSample.CATEGORY_MAX_COUNT + 1, "a"));
            eventModifyRequest.setCategories(categories);
            assertThatThrownBy(() -> ValidationUtil.validate(eventModifyRequest))
                    .isInstanceOf(ConstraintViolationException.class);
        }
    }
}