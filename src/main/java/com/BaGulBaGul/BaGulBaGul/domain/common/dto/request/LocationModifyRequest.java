package com.BaGulBaGul.BaGulBaGul.domain.common.dto.request;

import javax.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationModifyRequest {
    @Builder.Default
    private JsonNullable<String> fullLocation = JsonNullable.undefined();

    @Builder.Default
    private JsonNullable<String> abstractLocation = JsonNullable.undefined();

    @Builder.Default
    private JsonNullable<Float> latitudeLocation = JsonNullable.undefined();

    @Builder.Default
    private JsonNullable<Float> longitudeLocation = JsonNullable.undefined();

    @AssertTrue(message = "위도는 -90.0 ~ 90.0 사이의 소수여야 합니다.")
    private boolean isValidLatitudeLocation() {
        //존재하지 않거나 null인 경우는 무시
        return !latitudeLocation.isPresent() || latitudeLocation.get() == null ||
                (latitudeLocation.get() >= -90f && latitudeLocation.get() <= 90f);
    }

    @AssertTrue(message = "경도는 -180.0 ~ 180.0 사이의 소수여야 합니다.")
    private boolean isValidLongitudeLocation() {
        //존재하지 않거나 null인 경우는 무시
        return !longitudeLocation.isPresent() || latitudeLocation.get() == null ||
                (longitudeLocation.get() >= -180f && longitudeLocation.get() <= 180f);
    }
}
