package com.BaGulBaGul.BaGulBaGul.domain.common.sampledata;

import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.LocationModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.LocationRegisterRequest;
import org.openapitools.jackson.nullable.JsonNullable;

public class LocationSample {
    public static final String NORMAL_FULL_LOCATION = "서울시 영등포구 xxx로 xxx타워 x층";
    public static final String NORMAL_ABSTRACT_LOCATION = "서울시 영등포구";
    public static final Float NORMAL_LATITUDE_LOCATION = 33.83f;
    public static final Float NORMAL_LONGITUDE_LOCATION = 40.23f;
    public static LocationRegisterRequest getNormalRegisterRequest() {
        return LocationRegisterRequest.builder()
                .fullLocation(NORMAL_FULL_LOCATION)
                .abstractLocation(NORMAL_ABSTRACT_LOCATION)
                .latitudeLocation(NORMAL_LATITUDE_LOCATION)
                .longitudeLocation(NORMAL_LONGITUDE_LOCATION)
                .build();
    }
    public static LocationModifyRequest getNormalModifyRequest() {
        return LocationModifyRequest.builder()
                .fullLocation(JsonNullable.of(NORMAL_FULL_LOCATION))
                .abstractLocation(JsonNullable.of(NORMAL_ABSTRACT_LOCATION))
                .latitudeLocation(JsonNullable.of(NORMAL_LATITUDE_LOCATION))
                .longitudeLocation(JsonNullable.of(NORMAL_LONGITUDE_LOCATION))
                .build();
    }
    public static final String NORMAL2_FULL_LOCATION = "경기도 고양시 xxx로 xxx타워 x층";
    public static final String NORMAL2_ABSTRACT_LOCATION = "경기도 고양시";
    public static final Float NORMAL2_LATITUDE_LOCATION = 30.83f;
    public static final Float NORMAL2_LONGITUDE_LOCATION = 55.23f;
    public static LocationRegisterRequest getNormal2RegisterRequest() {
        return LocationRegisterRequest.builder()
                .fullLocation(NORMAL2_FULL_LOCATION)
                .abstractLocation(NORMAL2_ABSTRACT_LOCATION)
                .latitudeLocation(NORMAL2_LATITUDE_LOCATION)
                .longitudeLocation(NORMAL2_LONGITUDE_LOCATION)
                .build();
    }
    public static LocationModifyRequest getNormal2ModifyRequest() {
        return LocationModifyRequest.builder()
                .fullLocation(JsonNullable.of(NORMAL2_FULL_LOCATION))
                .abstractLocation(JsonNullable.of(NORMAL2_ABSTRACT_LOCATION))
                .latitudeLocation(JsonNullable.of(NORMAL2_LATITUDE_LOCATION))
                .longitudeLocation(JsonNullable.of(NORMAL2_LONGITUDE_LOCATION))
                .build();
    }
}
