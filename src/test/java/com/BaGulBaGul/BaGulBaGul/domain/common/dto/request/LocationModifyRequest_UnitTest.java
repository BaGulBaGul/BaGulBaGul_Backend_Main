package com.BaGulBaGul.BaGulBaGul.domain.common.dto.request;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.BaGulBaGul.BaGulBaGul.domain.common.sampledata.LocationSample;
import com.BaGulBaGul.BaGulBaGul.global.validation.ValidationUtil;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

class LocationModifyRequest_UnitTest {

    @Nested
    @DisplayName("검증 테스트")
    class Validation {
        @Test
        @DisplayName("정상")
        void shouldOK() {
            assertDoesNotThrow(() -> ValidationUtil.validate(LocationSample.getNormalModifyRequest()));
        }

        @Test
        @DisplayName("위도가 -90보다 작음")
        void shouldThrowConstraintViolationException_WhenLatitudeLessThanLimit() {
            LocationModifyRequest locationModifyRequest = LocationSample.getNormalModifyRequest();
            locationModifyRequest.setLatitudeLocation(JsonNullable.of(-90.1f));
            assertThatThrownBy(() -> ValidationUtil.validate(locationModifyRequest))
                    .isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("위도가 -90보다 작음")
        void shouldThrowConstraintViolationException_WhenLatitudeGreaterThanLimit() {
            LocationModifyRequest locationModifyRequest = LocationSample.getNormalModifyRequest();
            locationModifyRequest.setLatitudeLocation(JsonNullable.of(90.1f));
            assertThatThrownBy(() -> ValidationUtil.validate(locationModifyRequest))
                    .isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("경도가 -180보다 작음")
        void shouldThrowConstraintViolationException_WhenLongitudeLessThanLimit() {
            LocationModifyRequest locationModifyRequest = LocationSample.getNormalModifyRequest();
            locationModifyRequest.setLongitudeLocation(JsonNullable.of(-180.1f));
            assertThatThrownBy(() -> ValidationUtil.validate(locationModifyRequest))
                    .isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("경도가 180보다 큼")
        void shouldThrowConstraintViolationException_WhenLongitudeGreaterThanLimit() {
            LocationModifyRequest locationModifyRequest = LocationSample.getNormalModifyRequest();
            locationModifyRequest.setLongitudeLocation(JsonNullable.of(180.1f));
            assertThatThrownBy(() -> ValidationUtil.validate(locationModifyRequest))
                    .isInstanceOf(ConstraintViolationException.class);
        }
    }
}