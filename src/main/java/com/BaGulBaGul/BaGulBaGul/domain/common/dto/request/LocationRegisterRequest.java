package com.BaGulBaGul.BaGulBaGul.domain.common.dto.request;

import javax.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationRegisterRequest {

    //전체 주소
    private String fullLocation;

    //요약 주소
    private String abstractLocation;

    //위도
    private Float latitudeLocation;

    //경도
    private Float longitudeLocation;

    @AssertTrue(message = "위도는 -90.0 ~ 90.0 사이의 소수여야 합니다.")
    private boolean isValidLatitudeLocation() {
        return latitudeLocation == null || (latitudeLocation >= -90f && latitudeLocation <= 90f);
    }

    @AssertTrue(message = "경도는 -180.0 ~ 180.0 사이의 소수여야 합니다.")
    private boolean isValidLongitudeLocation() {
        return longitudeLocation == null || (longitudeLocation >= -180f && longitudeLocation <= 180f);
    }
}
