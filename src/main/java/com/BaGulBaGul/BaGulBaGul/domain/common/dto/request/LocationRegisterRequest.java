package com.BaGulBaGul.BaGulBaGul.domain.common.dto.request;

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
}
