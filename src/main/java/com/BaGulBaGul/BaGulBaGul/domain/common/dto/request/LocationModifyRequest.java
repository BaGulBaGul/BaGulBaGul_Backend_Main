package com.BaGulBaGul.BaGulBaGul.domain.common.dto.request;

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
}
